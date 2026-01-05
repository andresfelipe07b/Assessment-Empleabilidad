import { Injectable } from '@angular/core';
import Swal, { SweetAlertIcon, SweetAlertResult } from 'sweetalert2';

@Injectable({
    providedIn: 'root'
})
export class NotificationService {

    constructor() { }

    // Method to show success notification
    success(title: string, text: string = '') {
        return Swal.fire({
            title: title,
            text: text,
            icon: 'success',
            confirmButtonColor: 'var(--primary)',
            background: 'var(--card-bg)',
            color: 'var(--text-primary)',
            customClass: {
                popup: 'swal-custom-popup'
            }
        });
    }

    // Method to show error notification
    error(title: string, text: string = '') {
        return Swal.fire({
            title: title,
            text: text,
            icon: 'error',
            confirmButtonColor: 'var(--primary)',
            background: 'var(--card-bg)',
            color: 'var(--text-primary)',
            customClass: {
                popup: 'swal-custom-popup'
            }
        });
    }

    // Method to show confirmation dialog
    confirm(title: string, text: string, confirmButtonText: string = 'Yes, confirm'): Promise<SweetAlertResult> {
        return Swal.fire({
            title: title,
            text: text,
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: 'var(--primary)',
            cancelButtonColor: 'var(--text-secondary)',
            confirmButtonText: confirmButtonText,
            background: 'var(--card-bg)',
            color: 'var(--text-primary)',
            customClass: {
                popup: 'swal-custom-popup',
                confirmButton: 'btn btn-primary',
                cancelButton: 'btn'
            }
        });
    }

    // Toast notification (top right)
    toast(title: string, icon: SweetAlertIcon = 'success') {
        const Toast = Swal.mixin({
            toast: true,
            position: 'top-end',
            showConfirmButton: false,
            timer: 3000,
            timerProgressBar: true,
            background: 'var(--card-bg)',
            color: 'var(--text-primary)',
            didOpen: (toast) => {
                toast.addEventListener('mouseenter', Swal.stopTimer);
                toast.addEventListener('mouseleave', Swal.resumeTimer);
            }
        });

        Toast.fire({
            icon: icon,
            title: title
        });
    }
}
