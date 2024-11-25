import axios from 'axios';

const REST_API_URL = 'http://localhost:8080/api/tasks';

export const listTask = async () => await axios.get(REST_API_URL);

export const createTask = async (task) => await axios.post(REST_API_URL, task);

export const getTask = async (id) => await axios.get(`${REST_API_URL}/${id}`);

export const updateTask = async (id, task) => await axios.post(`${REST_API_URL}/${id}`, task);

export const deleteTask = async (id) => await axios.delete(`${REST_API_URL}/${id}`);