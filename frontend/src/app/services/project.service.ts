import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Project {
    id: string;
    name: string;
    status: 'DRAFT' | 'ACTIVE';
    tasks?: Task[];
}

export interface Task {
    id: string;
    title: string;
    completed: boolean;
    projectId: string;
}

@Injectable({
    providedIn: 'root'
})
export class ProjectService {
    private apiUrl = '/api';

    constructor(private http: HttpClient) { }

    getProjects(): Observable<Project[]> {
        return this.http.get<Project[]>(`${this.apiUrl}/projects`);
    }

    getProject(id: string): Observable<Project> {
        return this.http.get<Project>(`${this.apiUrl}/projects/${id}`);
    }

    createProject(name: string): Observable<Project> {
        return this.http.post<Project>(`${this.apiUrl}/projects`, { name });
    }

    activateProject(id: string): Observable<Project> {
        return this.http.patch<Project>(`${this.apiUrl}/projects/${id}/activate`, {});
    }

    createTask(projectId: string, title: string): Observable<Task> {
        return this.http.post<Task>(`${this.apiUrl}/projects/${projectId}/tasks`, { title });
    }

    completeTask(taskId: string): Observable<Task> {
        return this.http.patch<Task>(`${this.apiUrl}/tasks/${taskId}/complete`, {});
    }

    deleteTask(taskId: string): Observable<void> {
        return this.http.delete<void>(`${this.apiUrl}/tasks/${taskId}`);
    }


    getTasks(projectId: string): Observable<Task[]> {
        return this.http.get<Task[]>(`${this.apiUrl}/projects/${projectId}/tasks`);
    }
}
