import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { Project, ProjectService, Task } from '../../services/project.service';
import { FormsModule } from '@angular/forms';
import { NotificationService } from '../../services/notification.service';
import { LoaderComponent } from '../../components/loader/loader.component';
import { NavbarComponent } from '../../components/navbar/navbar.component';

@Component({
    selector: 'app-project-details',
    standalone: true,
    imports: [CommonModule, FormsModule, RouterModule, LoaderComponent, NavbarComponent],
    templateUrl: './project-details.component.html',
    styleUrls: ['./project-details.component.css']
})
export class ProjectDetailsComponent implements OnInit {
    project: Project | null = null;
    tasks: Task[] = [];
    newTaskTitle = '';
    loading = true;

    constructor(
        private route: ActivatedRoute,
        private projectService: ProjectService,
        private notificationService: NotificationService
    ) { }

    ngOnInit() {
        const id = this.route.snapshot.paramMap.get('id');
        if (id) {
            this.loadData(id);
        }
    }

    loadData(projectId: string) {
        this.projectService.getProject(projectId).subscribe(project => {
            console.log('Found Project:', project);
            this.project = project;
            this.loading = false;
        }, error => {
            console.error('Error loading project', error);
            this.loading = false;
        });

        this.projectService.getTasks(projectId).subscribe(tasks => {
            this.tasks = tasks;
        });
    }

    addTask() {
        if (!this.newTaskTitle || !this.project) return;
        this.projectService.createTask(this.project.id, this.newTaskTitle).subscribe(task => {
            this.tasks.push(task);
            this.newTaskTitle = '';
            this.notificationService.toast('Task added', 'success');
        });
    }

    completeTask(task: Task) {
        if (task.completed) return;
        this.projectService.completeTask(task.id).subscribe(updated => {
            task.completed = true;
            this.notificationService.toast('Task completed', 'success');
        });
    }

    activateProject() {
        if (!this.project) return;
        this.notificationService.confirm('Activate Project?', 'This will optimize the project workspace.').then((result) => {
            if (result.isConfirmed) {
                this.projectService.activateProject(this.project!.id).subscribe(p => {
                    if (this.project) this.project.status = 'ACTIVE';
                    this.notificationService.success('Project Activated', 'Use the workspace now!');
                }, err => {
                    this.notificationService.error('Cannot Activate', err.error?.message || 'Unknown error');
                });
            }
        });
    }
}
