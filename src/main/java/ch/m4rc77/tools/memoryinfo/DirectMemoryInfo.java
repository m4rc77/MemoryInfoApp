package ch.m4rc77.tools.memoryinfo;

class DirectMemoryInfo {

   private double directUsedMemoryMB;
   private double directMemoryCount;
   private double directFreeMemoryNowMB;
   private double directMaxMemoryMB;
   private double directMemoryUsagePercent;

   private String info;

   public double getDirectUsedMemoryMB () {
      return directUsedMemoryMB;
   }

   public void setDirectUsedMemoryMB (double directUsedMemoryMB) {
      this.directUsedMemoryMB = directUsedMemoryMB;
   }

   public double getDirectMemoryCount () {
      return directMemoryCount;
   }

   public void setDirectMemoryCount (double directMemoryCount) {
      this.directMemoryCount = directMemoryCount;
   }

   public double getDirectFreeMemoryNowMB () {
      return directFreeMemoryNowMB;
   }

   public void setDirectFreeMemoryNowMB (double directFreeMemoryNowMB) {
      this.directFreeMemoryNowMB = directFreeMemoryNowMB;
   }

   public double getDirectMaxMemoryMB () {
      return directMaxMemoryMB;
   }

   public void setDirectMaxMemoryMB (double directMaxMemoryMB) {
      this.directMaxMemoryMB = directMaxMemoryMB;
   }

   public double getDirectMemoryUsagePercent () {
      return directMemoryUsagePercent;
   }

   public void setDirectMemoryUsagePercent (double directMemoryUsagePercent) {
      this.directMemoryUsagePercent = directMemoryUsagePercent;
   }

   public String getInfo () {
      return info;
   }

   public void setInfo (String info) {
      this.info = info;
   }
}
