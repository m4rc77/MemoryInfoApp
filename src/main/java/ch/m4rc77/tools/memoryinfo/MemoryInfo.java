package ch.m4rc77.tools.memoryinfo;

class MemoryInfo {

   private String info;

   private String status;

   private String statusInfo;

   private double totalMemoryMB;

   private int availableProcessorCores;

   private long openFileDescriptorCount;

   private HeapMemoryInfo heapMemoryInfo;

   private DirectMemoryInfo directMemoryInfo;

   public String getInfo () {
      return info;
   }

   public void setInfo (String info) {
      this.info = info;
   }

   public String getStatus () {
      return status;
   }

   public void setStatus (String status) {
      this.status = status;
   }

   public String getStatusInfo () {
      return statusInfo;
   }

   public void setStatusInfo (String statusInfo) {
      this.statusInfo = statusInfo;
   }

   public double getTotalMemoryMB () {
      return totalMemoryMB;
   }

   public void setTotalMemoryMB (double totalMemoryMB) {
      this.totalMemoryMB = totalMemoryMB;
   }

   public int getAvailableProcessorCores () {
      return availableProcessorCores;
   }

   public void setAvailableProcessorCores (int availableProcessorCores) {
      this.availableProcessorCores = availableProcessorCores;
   }

   public long getOpenFileDescriptorCount () {
      return openFileDescriptorCount;
   }

   public void setOpenFileDescriptorCount (long openFileDescriptorCount) {
      this.openFileDescriptorCount = openFileDescriptorCount;
   }

   public HeapMemoryInfo getHeapMemoryInfo () {
      return heapMemoryInfo;
   }

   public void setHeapMemoryInfo (HeapMemoryInfo heapMemoryInfo) {
      this.heapMemoryInfo = heapMemoryInfo;
   }

   public DirectMemoryInfo getDirectMemoryInfo () {
      return directMemoryInfo;
   }

   public void setDirectMemoryInfo (DirectMemoryInfo directMemoryInfo) {
      this.directMemoryInfo = directMemoryInfo;
   }
}
