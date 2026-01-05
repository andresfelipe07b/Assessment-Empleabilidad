import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { NotificationService } from '../../services/notification.service';

@Component({
    selector: 'app-login',
    standalone: true,
    imports: [CommonModule, FormsModule, RouterModule],
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css']
})
export class LoginComponent {
    email = '';
    password = '';
    error = '';
    isRegistering = false;
    username = '';

    constructor(
        private authService: AuthService,
        private router: Router,
        private notificationService: NotificationService
    ) { }

    onSubmit() {
        this.error = '';
        if (this.isRegistering) {
            if (!this.username) {
                this.error = 'Username is required';
                return;
            }
            this.authService.register({ username: this.username, email: this.email, password: this.password }).subscribe({
                next: () => {
                    this.isRegistering = false;
                    this.notificationService.success('Welcome!', 'Registration successful. Please login.');
                },
                error: (err) => {
                    // Use notification for errors too for consistency, or keep inline error? 
                    // User asked to remove the alert. I'll replace the success alert. 
                    // I will also show a toast for error but keep the inline text as fallback.
                    this.error = 'Registration failed. ' + (err.error?.message || '');
                    this.notificationService.error('Registration Failed', err.error?.message || 'Please try again.');
                }
            });
        } else {
            this.authService.login({ email: this.email, password: this.password }).subscribe({
                next: () => this.router.navigate(['/dashboard']),
                error: (err) => this.error = 'Login failed. ' + (err.error?.message || '')
            });
        }
    }

    toggleMode() {
        this.isRegistering = !this.isRegistering;
        this.error = '';
    }
}
