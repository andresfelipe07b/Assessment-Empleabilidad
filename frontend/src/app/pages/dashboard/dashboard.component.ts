import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Project, ProjectService } from '../../services/project.service';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { NotificationService } from '../../services/notification.service';
import { NavbarComponent } from '../../components/navbar/navbar.component';
import { LoaderComponent } from '../../components/loader/loader.component';
import { AuthService } from '../../services/auth.service';

@Component({
    selector: 'app-dashboard',
    standalone: true,
    imports: [CommonModule, RouterModule, FormsModule, NavbarComponent, LoaderComponent],
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
    projects: Project[] = [];
    newProjectName = '';
    error = '';
    loading = true;

    constructor(private projectService: ProjectService, private authService: AuthService) { }

    ngOnInit() {
        this.loadProjects();
    }

    loadProjects() {
        this.loading = true;
        this.projectService.getProjects().subscribe({
            next: (data) => {
                this.projects = data;
                this.loading = false;
            },
            error: (err) => {
                this.error = 'Failed to load projects';
                this.loading = false;
            }
        });
    }

    createProject() {
        if (!this.newProjectName) return;
        this.projectService.createProject(this.newProjectName).subscribe({
            next: (project) => {
                this.projects.push(project);
                this.newProjectName = '';
            },
            error: (err) => alert('Failed to create project')
        });
    }

    logout() {
        this.authService.logout();
        window.location.reload();
    }
}
