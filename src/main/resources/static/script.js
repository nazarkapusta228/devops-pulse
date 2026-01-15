class Dashboard {
    constructor() {
        this.systemDataUrl = '/api/system';
        this.init();
    }

    init() {
        this.bindEvents();
        this.loadSystemData();
        this.startAutoRefresh();
    }

    bindEvents() {
        document.getElementById('refresh-system').addEventListener('click', () => {
            this.loadSystemData();
        });

        document.getElementById('load-github').addEventListener('click', () => {
            this.loadGitHubData();
        });

        document.getElementById('github-username').addEventListener('keypress', (e) => {
            if (e.key === 'Enter') {
                this.loadGitHubData();
            }
        });
    }

    async loadSystemData() {
        const statusElement = document.getElementById('system-status');
        const dataElement = document.getElementById('system-data');
        const buttonElement = document.getElementById('refresh-system');

        this.setStatus(statusElement, '<i class="fas fa-sync-alt fa-spin"></i> Loading system metrics...', 'info');
        buttonElement.disabled = true;
        buttonElement.innerHTML = '<i class="fas fa-sync-alt fa-spin"></i> Loading...';

        try {
            const response = await fetch(this.systemDataUrl);

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const data = await response.json();

            const timestamp = new Date().toLocaleTimeString([], {hour: '2-digit', minute:'2-digit', second:'2-digit'});
            this.setStatus(statusElement, `<i class="fas fa-check-circle"></i> Last updated: ${timestamp}`, 'success');
            this.renderSystemData(dataElement, data);

        } catch (error) {
            this.setStatus(statusElement, `<i class="fas fa-exclamation-triangle"></i> Error loading system data`, 'error');
            this.renderError(dataElement, error.message);
        } finally {
            buttonElement.disabled = false;
            buttonElement.innerHTML = '<i class="fas fa-redo"></i> Refresh System Data';
        }
    }

    async loadGitHubData() {
        const username = document.getElementById('github-username').value.trim();
        const statusElement = document.getElementById('github-status');
        const dataElement = document.getElementById('github-data');
        const buttonElement = document.getElementById('load-github');

        if (!username) {
            this.setStatus(statusElement, '<i class="fas fa-exclamation-circle"></i> Please enter a username', 'error');
            return;
        }

        this.setStatus(statusElement, '<i class="fas fa-sync-alt fa-spin"></i> Loading GitHub data...', 'info');
        buttonElement.disabled = true;
        buttonElement.innerHTML = '<i class="fas fa-sync-alt fa-spin"></i> Loading...';
        dataElement.innerHTML = '';

        try {
            const response = await fetch(`/api/github/${username}`);
            const data = await response.json();

            if (data.success) {
                this.setStatus(statusElement, '<i class="fas fa-check-circle"></i> Data loaded successfully', 'success');
                this.renderGitHubData(dataElement, data);
            } else {
                this.setStatus(statusElement, '<i class="fas fa-exclamation-triangle"></i> Error loading GitHub data', 'error');
                this.renderError(dataElement, data.message);
            }

        } catch (error) {
            this.setStatus(statusElement, '<i class="fas fa-exclamation-triangle"></i> Network error', 'error');
            this.renderError(dataElement, error.message);
        } finally {
            buttonElement.disabled = false;
            buttonElement.innerHTML = '<i class="fas fa-search"></i> Load GitHub Data';
        }
    }

    renderSystemData(container, data) {
        const metrics = [
            {
                label: 'CPU Usage',
                value: data.cpuUsage,
                rawValue: data.cpuUsageValue,
                type: 'progress',
                icon: 'cpu-icon',
                description: 'Processor utilization',
                unit: '%'
            },
            {
                label: 'Memory Usage',
                value: data.memoryUsage,
                rawValue: data.memoryUsageValue,
                type: 'progress',
                icon: 'memory-icon',
                description: 'RAM consumption',
                unit: '%'
            },
            {
                label: 'Disk Usage',
                value: data.diskUsage,
                rawValue: data.diskUsageValue,
                type: 'progress',
                icon: 'disk-icon',
                description: 'Storage space used',
                unit: '%'
            },
            {
                label: 'Total Memory',
                value: data.totalMemoryGB,
                type: 'text',
                icon: 'memory-total-icon',
                description: 'Installed RAM',
                unit: 'GB'
            },
            {
                label: 'Total Disk Space',
                value: data.totalDiskSpaceGB,
                type: 'text',
                icon: 'disk-total-icon',
                description: 'Total storage capacity',
                unit: 'GB'
            },
            {
                label: 'Running Processes',
                value: data.processCount,
                type: 'text',
                icon: 'process-icon',
                description: 'Active system processes'
            },
            {
                label: 'System Uptime',
                value: data.uptimeHours,
                type: 'text',
                icon: 'uptime-icon',
                description: 'Time since last reboot',
                unit: 'hours'
            }
        ];

        container.innerHTML = metrics.map(metric => this.createSystemMetricElement(metric)).join('');
    }

    createSystemMetricElement(metric) {
        const iconClass = metric.icon || 'cpu-icon';
        const iconSymbol = this.getMetricIcon(metric.label);

        if (metric.type === 'progress') {
            const color = this.getProgressColor(metric.rawValue);
            const status = this.getProgressStatus(metric.rawValue);

            return `
                <div class="metric">
                    <div class="metric-info">
                        <div class="metric-icon ${iconClass}">
                            <i class="${iconSymbol}"></i>
                        </div>
                        <div class="metric-details">
                            <div class="metric-label">${metric.label}</div>
                            <div class="metric-description">${metric.description}</div>
                            <div class="progress-container">
                                <div class="progress-bar">
                                    <div class="progress-fill" style="width: ${metric.value}%; background: ${color};"></div>
                                </div>
                                <div class="status-indicator">
                                    <span class="status-dot" style="background-color: ${color};"></span>
                                    <span class="status-text">${status}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="metric-value percentage" style="color: ${color}">
                        ${metric.value}<span class="unit">${metric.unit || ''}</span>
                    </div>
                </div>
            `;
        } else {
            return `
                <div class="metric">
                    <div class="metric-info">
                        <div class="metric-icon ${iconClass}">
                            <i class="${iconSymbol}"></i>
                        </div>
                        <div class="metric-details">
                            <div class="metric-label">${metric.label}</div>
                            <div class="metric-description">${metric.description}</div>
                        </div>
                    </div>
                    <div class="metric-value">
                        ${metric.value}<span class="unit">${metric.unit || ''}</span>
                    </div>
                </div>
            `;
        }
    }

    renderGitHubData(container, data) {
        const metrics = [
            { label: 'Username', value: data.username, icon: 'fas fa-user', color: '#667eea' },
            { label: 'Name', value: data.name, icon: 'fas fa-id-card', color: '#4ECDC4' },
            { label: 'Public Repositories', value: data.publicRepos, icon: 'fas fa-code-branch', color: '#FFD166' },
            { label: 'Followers', value: data.followers, icon: 'fas fa-users', color: '#06D6A0' }
        ];

        if (data.location && data.location !== 'Not specified') {
            metrics.push({ label: 'Location', value: data.location, icon: 'fas fa-map-marker-alt', color: '#118AB2' });
        }

        container.innerHTML = metrics.map(metric => this.createGitHubMetricElement(metric)).join('');
    }

    createGitHubMetricElement(metric) {
        return `
            <div class="metric">
                <div class="metric-info">
                    <div class="metric-icon" style="background: ${metric.color}">
                        <i class="${metric.icon}"></i>
                    </div>
                    <div class="metric-details">
                        <div class="metric-label">${metric.label}</div>
                    </div>
                </div>
                <div class="metric-value">
                    ${metric.value}
                </div>
            </div>
        `;
    }

    getMetricIcon(label) {
        const icons = {
            'CPU Usage': 'fas fa-microchip',
            'Memory Usage': 'fas fa-memory',
            'Disk Usage': 'fas fa-hdd',
            'Total Memory': 'fas fa-memory',
            'Total Disk Space': 'fas fa-database',
            'Running Processes': 'fas fa-tasks',
            'System Uptime': 'fas fa-clock'
        };
        return icons[label] || 'fas fa-chart-line';
    }

    getProgressColor(value) {
        if (value <= 60) return '#28a745';
        if (value <= 80) return '#ffc107';
        return '#dc3545';
    }

    getProgressStatus(value) {
        if (value <= 60) return 'Optimal';
        if (value <= 80) return 'Moderate';
        return 'High';
    }

    setStatus(element, message, type) {
        element.innerHTML = message;
        element.className = `status ${type}`;
    }

    renderError(container, message) {
        container.innerHTML = `
            <div class="metric">
                <div class="metric-info">
                    <div class="metric-icon" style="background: #dc3545">
                        <i class="fas fa-exclamation-circle"></i>
                    </div>
                    <div class="metric-details">
                        <div class="metric-label">Error</div>
                        <div class="metric-description">${message}</div>
                    </div>
                </div>
            </div>
        `;
    }

    startAutoRefresh() {
        setInterval(() => {
            this.loadSystemData();
        }, 10000);
    }
}

window.addEventListener('DOMContentLoaded', () => {
    new Dashboard();
});