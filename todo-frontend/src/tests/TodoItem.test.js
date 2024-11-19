import React from 'react';
import { render, screen } from '@testing-library/react';
import ListTask from '../components/ListTask/ListTask';

test('renders TodoItem with title', () => {
    render(<ListTask title="Test Task" />);
    const todoElement = screen.getByText(/To-Do List/i);
    expect(todoElement).toBeInTheDocument();
});
