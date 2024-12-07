import React, { useState } from "react";
import { createTask } from "../../services/TaskService";
import "./AddTask.css";

const AddTask = ({ onTaskAdded }) => {
    const [taskTitle, setTaskTitle] = useState("");

    const handleSubmit = (e) => {
        console.log(process.env.REACT_APP_BASE_API);
        e.preventDefault();
        const task = {
            title: taskTitle,
            completed: false,
        };
        createTask(task)
            .then((response) => {
                console.log("Task added:", response.data);
                setTaskTitle("");
                if (onTaskAdded) {
                    onTaskAdded(response.data);
                }
            })
            .catch((error) => console.error("There was an error adding the task!", error));
    };

    return (
        <div className="add-task-container">
            <h3 className="add-task-header">Add a New Task</h3>
            <form onSubmit={handleSubmit} className="add-task-form">
                <input
                    type="text"
                    value={taskTitle}
                    onChange={(e) => setTaskTitle(e.target.value)}
                    placeholder="Enter task title"
                    required
                    className="add-task-input"
                />
                <button type="submit" className="add-task-button">
                    Add Task
                </button>
            </form>
        </div>
    );
};

export default AddTask;
