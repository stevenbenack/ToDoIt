package com.stevenbenack.todoit.storage;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskStorage {
    private static TaskStorage taskStorage;

//    private Context context;
    private SQLiteDatabase database;

	private TaskStorage(Context c) {
		Context context = c.getApplicationContext();
		database = new TaskDbHelper(context).getWritableDatabase();
	}

    public static TaskStorage get(Context context){
        if(taskStorage == null){
            taskStorage = new TaskStorage(context);
        }
        return taskStorage;
    }

     public List<Task> getTasks() {
    	List<Task> tasks = new ArrayList<>();

	     try (TasksCursorWrapper cursor = queryTasks(null, null)) {
		     cursor.moveToFirst();
		     while (!cursor.isAfterLast()) {
			     tasks.add(cursor.getTask());
			     cursor.moveToNext();
		     }
	     }
        return tasks;
    }

    public Task getTask(UUID id){
	    try (TasksCursorWrapper cursor = queryTasks(
			    TaskDbSchema.TaskTable.Cols.UUID + " = ?",
			    new String[]{id.toString()}
	    )) {
		    if(cursor.getCount() == 0) {
			    return null;
		    }
		    cursor.moveToFirst();
		    return cursor.getTask();
	    }
    }

    public void updateTask(Task task) {
    	String uuidString = task.getId().toString();
    	ContentValues values = getContentValues(task);

    	database.update(TaskDbSchema.TaskTable.NAME, values,
			    TaskDbSchema.TaskTable.Cols.UUID  + " = ?",
			    new String[] {uuidString});
    }

    private TasksCursorWrapper queryTasks(String whereClause, String[] whereArgs) {
	    Cursor cursor = database.query(
			    TaskDbSchema.TaskTable.NAME,
			    null, // for all columns
			    whereClause,
			    whereArgs,
			    null,
			    null,
			    null
	    );
		return new TasksCursorWrapper(cursor);
    }

    public void addTask(Task task) {
    	ContentValues values = getContentValues(task);
    	database.insert(TaskDbSchema.TaskTable.NAME, null, values);
    }

    // ContentValues used to key-value write / store to db
    private static ContentValues getContentValues(Task task) {
    	ContentValues values = new ContentValues();

    	values.put(TaskDbSchema.TaskTable.Cols.UUID, task.getId().toString());
    	values.put(TaskDbSchema.TaskTable.Cols.TITLE, task.getTitle());
    	values.put(TaskDbSchema.TaskTable.Cols.DESCRIPTION, task.getDescription());
    	values.put(TaskDbSchema.TaskTable.Cols.CREATED, task.getCreatedDateTime().toString());
    	values.put(TaskDbSchema.TaskTable.Cols.DUE, task.getDueDateTime().getTime());
	    values.put(TaskDbSchema.TaskTable.Cols.PRIORITY, task.getPriority());
	    values.put(TaskDbSchema.TaskTable.Cols.DONE, task.getIsDone() ? 1 : 0);

	    return values;
    }
}
