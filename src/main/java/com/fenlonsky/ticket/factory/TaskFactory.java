package com.fenlonsky.ticket.factory;

import com.fenlonsky.ticket.model.Task;
import com.fenlonsky.ticket.utils.FileIOUtil;
import com.google.common.collect.Lists;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * Created by fenlon on 15-12-14.
 */
public class TaskFactory {
    private static final Logger logger = LoggerFactory.getLogger(TaskFactory.class);
    private static final List<Task> tasks = Lists.newArrayList();
    private static TicketConfig config;

    private TaskFactory() {

    }

    private static class Holder {
        private static final TaskFactory INSTANCE = new TaskFactory();
    }

    public static TaskFactory getInstance(TicketConfig ticketConfig) {
        config = ticketConfig;
        return Holder.INSTANCE;
    }

    public List<Task> getTasks() {
//        String json = FileIOUtil.readFile(config.getConfDirKey() + File.separator + "task.json");
        String json = FileIOUtil.readFile("D:\\Java\\workspace\\ticket\\conf\\task.json");
        if (json.equals("")) {
            return null;
        }
        JSONArray arrs = new JSONObject(json.toString()).getJSONArray("tasks");
        JSONObject jsonTask = null;
        Task task;
        List<Task> tasks = Lists.newArrayList();
        for (int i = 0; i < arrs.length(); i++) {
            jsonTask = arrs.getJSONObject(i);
            task = mapper2Obj(jsonTask);
            tasks.add(task);
        }
        return tasks;
    }

    private Task mapper2Obj(JSONObject jsonTask) {
        Task task = new Task();
        task.setName((String) jsonTask.get("name"));
        task.setStartDate(jsonTask.getString("startDate"));
        task.setEndDate(jsonTask.getString("endDate"));
        task.setUrlTemplate(jsonTask.getString("urlTemplate"));
        task.setTos(parseJsonArray(jsonTask.getJSONArray("tos")));
        task.setStations(parseJsonArray(jsonTask.getJSONArray("stations")));
        return task;
    }

    private List<String> parseJsonArray(JSONArray array) {
        List<String> list = Lists.newArrayList();
        for (int i = 0; i < array.length(); i++) {
            list.add(array.getString(i));
        }
        return list;
    }
}
