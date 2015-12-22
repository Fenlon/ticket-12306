package com.fenlonsky.ticket;

import com.fenlonsky.ticket.factory.*;
import com.fenlonsky.ticket.model.Email;
import com.fenlonsky.ticket.model.Task;
import com.fenlonsky.ticket.utils.TimeUtil;
import com.fenlonsky.ticket.utils.UrlUtil;
import com.google.common.base.Optional;
import com.google.common.primitives.Booleans;
import com.google.common.util.concurrent.Striped;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.json.JSONObject;
import org.yaml.snakeyaml.nodes.CollectionNode;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;

/**
 * Created by fenlon on 15-12-12.
 */
public class Main {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();

        final TicketConfig ticketConfig = TicketConfigFactory.getTicketConf();
        List<Task> tasks = TaskFactory.getInstance(ticketConfig).getTasks();
        final BlockingQueue emailQueue = new LinkedBlockingDeque<Email>();
        //eat
        executorService.execute(new Runnable() {
            PostFactory<Email> postFactory = new EmailPostFactory(ticketConfig, emailQueue);

            @Override
            public void run() {
                postFactory.start();
            }
        });

        //do task
        for (final Task task : tasks) {
            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder()
                    .setNameFormat("scheduler thread %s").build());
            executor.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    Long start = Timestamp.valueOf(task.getStartDate()).getTime();
                    Long end = Timestamp.valueOf(task.getEndDate()).getTime();
                    if (start > end) {
                        return;
                    }
                    doTask(task);
                }
            }, 10, 60, TimeUnit.SECONDS);
        }


        //post
        Sender sender = SendFactory.createMailSender(emailQueue);
        sender.send("wokao", new String[]{"935057327@qq.com", "853555703@qq.com"});


        try {
            Thread.currentThread().sleep(1000);
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void doTask(Task task) {

        try {
            for (String to : task.getStations()) {
                String url = UrlUtil.fillUrl(task.getUrlTemplate(), task.getStartDate(), to);
                Optional<String> result = HttpUtil.doGet(url);
                if (!result.isPresent()) {
                    return;
                }

                JSONObject data = new JSONObject(result.get()).getJSONObject("data").getJSONObject("queryLeftNewDTO");
                if (data == null) {
                    return;
                }

                //封装成对象
                Checi checi = new Checi();
                checi.setStationTrainCode(data.getString("station_train_code"));
                checi.setFromStationName(data.getString("start_station_name"));
                checi.setToStationName(data.getString("end_station_name"));
                checi.setStartTime(data.getString("start_time"));
                checi.setArriveTime(data.getString("arrive_time"));
                checi.setLishi(data.getString("lishi"));
                checi.setYzNum(data.getString("yz_num"));
                checi.setRzNum(data.getString("rz_num"));
                checi.setYwNum(data.getString("yw_num"));
                checi.setRwNum(data.getString("rw_num"));
                checi.setGrNum(data.getString("gr_num"));
                checi.setZyNum(data.getString("zy_num"));
                checi.setZeNum(data.getString("ze_num"));
                checi.setTzNum(data.getString("tz_num"));
                checi.setGgNum(data.getString("gg_num"));
                checi.setYbNum(data.getString("yb_num"));
                checi.setWzNum(data.getString("wz_num"));
                checi.setQtNum(data.getString("qt_num"));
                checi.setSwzNum(data.getString("swz_num"));

                //判断是否有车票(封装成对象是为了以后更好的设计，现在先按着这个来实现)


                StringBuffer info = new StringBuffer("");
                if (checi.getYzNum() != null && !"无".equals(checi.getYzNum()) && !"--".equals(checi.getYzNum())) {
                    info.append("硬座有：　" + checi.getYzNum() + " 张\n");
                }

                if (checi.getRzNum() != null && !"无".equals(checi.getRzNum()) && !"--".equals(checi.getRzNum())) {
                    info.append("软座有：　" + checi.getRzNum() + " 张\n");
                }

                if (checi.getYwNum() != null && !"无".equals(checi.getYwNum()) && !"--".equals(checi.getYwNum())) {
                    info.append("硬卧有：　" + checi.getYwNum() + " 张\n");
                }

                if (checi.getRwNum() != null && !"无".equals(checi.getRwNum()) && !"--".equals(checi.getRwNum())) {
                    info.append("软卧有：　" + checi.getRwNum() + " 张\n");
                }

                if (checi.getGrNum() != null && !"无".equals(checi.getGrNum()) && !"--".equals(checi.getGrNum())) {
                    info.append("高级软卧有：　" + checi.getGrNum() + " 张\n");
                }

                if (checi.getZyNum() != null && !"无".equals(checi.getZyNum()) && !"--".equals(checi.getZyNum())) {
                    info.append("zy有：　" + checi.getZyNum() + " 张\n");
                }

                if (checi.getZeNum() != null && !"无".equals(checi.getZeNum()) && !"--".equals(checi.getZeNum())) {
                    info.append("ze有：　" + checi.getZeNum() + " 张\n");
                }

                if (checi.getTzNum() != null && !"无".equals(checi.getTzNum()) && !"--".equals(checi.getTzNum())) {
                    info.append("特等座有：　" + checi.getTzNum() + " 张\n");
                }

                if (checi.getGgNum() != null && !"无".equals(checi.getGgNum()) && !"--".equals(checi.getGgNum())) {
                    info.append("gg有：　" + checi.getGgNum() + " 张\n");
                }

                if (checi.getYbNum() != null && !"无".equals(checi.getYbNum()) && !"--".equals(checi.getYbNum())) {
                    info.append("yb有：　" + checi.getYbNum() + " 张\n");
                }


                if (checi.getWzNum() != null && !"无".equals(checi.getWzNum()) && !"--".equals(checi.getWzNum())) {
                    info.append("无座有：　" + checi.getWzNum() + " 张\n");
                }

                if (checi.getQtNum() != null && !"无".equals(checi.getQtNum()) && !"--".equals(checi.getQtNum())) {
                    info.append("qt有：　" + checi.getQtNum() + " 张\n");
                }

                if (checi.getSwzNum() != null && !"无".equals(checi.getSwzNum()) && !"--".equals(checi.getSwzNum())) {
                    info.append("swz有：　" + checi.getSwzNum() + " 张\n");
                }

                if ("".equals(info)) {
                    return;
                }


                StringBuffer content = new StringBuffer("有票啦:\n");
                content.append("日期为: " + task.getStartDate() + "\n");
                content.append("开车时间为：　" + checi.getStartTime() + "   " + "到达时间为：　" + checi.getArriveTime() + "\n");

                content.append(info);


            }

        } catch (Exception e) {
            return;
        }
    }
}
