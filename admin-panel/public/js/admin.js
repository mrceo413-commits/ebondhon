// Admin panel JS - minimal for now
document.addEventListener('DOMContentLoaded', function() {
    // Auto-dismiss alerts after 3 seconds
    const alerts = document.querySelectorAll('.alert-success, .alert-info');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.transition = 'opacity 0.5s';
            alert.style.opacity = '0';
            setTimeout(() => alert.remove(), 500);
        }, 3000);
    });
});
