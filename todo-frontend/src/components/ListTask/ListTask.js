import React, { useEffect, useState } from 'react';
import { listTask, deleteTask, updateTask, getTasksByUser } from '../../services/TaskService';
import AddTask from '../AddTask/AddTask';
import './ListTask.css';

const ListTask = () => {
    const [tasks, setTasks] = useState([]);

    const fetchTasks = () => {
        listTask().then((response) => {
            if (response.data) {
                setTasks(response.data);
            }
        });
    };

    const addNewTask = (newTask) => {

        setTasks([...tasks, newTask]);
    };

    const removeTask = (id) => {
        deleteTask(id).then(() => {
            setTasks(tasks.filter((task) => task.id !== id));
        });
    };

    const toggleCompletion = (task) => {
        const updatedTask = { ...task, completed: !task.completed };
        updateTask(task.id, updatedTask).then(() => {
            setTasks(tasks.map((t) => (t.id === task.id ? updatedTask : t)));
        });
    };

    const editTask = (id, newTitle) => {
        const updatedTask = tasks.find((task) => task.id === id);
        updatedTask.title = newTitle;
        updateTask(id, updatedTask).then(() => {
            setTasks([...tasks]);
        });
    };

    useEffect(() => {
        fetchTasks();
    }, []);

    return (
        <div className="task-container">
            <h1 className="task-header">To-Do List</h1>
            <AddTask onTaskAdded={addNewTask} />
            <ul className="task-list">
                {tasks.map((task) => (
                    <li key={task.id} className="task-item">
                        <input
                            type="checkbox"
                            checked={task.completed}
                            onChange={() => toggleCompletion(task)}
                            className="task-checkbox"
                        />
                        <input
                            type="text"
                            value={task.title}
                            onChange={(e) => editTask(task.id, e.target.value)}
                            className={`task-title ${task.completed ? 'completed' : ''}`}
                        />
                        <button
                            onClick={() => removeTask(task.id)}
                            className="delete-button"
                        >
                            Delete
                        </button>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default ListTask;