package com.cts.notificationservice.dto;

public class NotificationStatsDTO {
    private long totalNotifications;
    private long unreadNotifications;
    private long failedNotifications;
    private long sentNotifications;
    
    public long getTotalNotifications() { return totalNotifications; }
    public void setTotalNotifications(long totalNotifications) { this.totalNotifications = totalNotifications; }
    
    public long getUnreadNotifications() { return unreadNotifications; }
    public void setUnreadNotifications(long unreadNotifications) { this.unreadNotifications = unreadNotifications; }
    
    public long getFailedNotifications() { return failedNotifications; }
    public void setFailedNotifications(long failedNotifications) { this.failedNotifications = failedNotifications; }
    
    public long getSentNotifications() { return sentNotifications; }
    public void setSentNotifications(long sentNotifications) { this.sentNotifications = sentNotifications; }
}