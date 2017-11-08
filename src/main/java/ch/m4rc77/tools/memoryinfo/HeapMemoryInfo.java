package ch.m4rc77.tools.memoryinfo;

class HeapMemoryInfo
{
   private double heapUsedMemoryMB;
   private double heapFreeMemoryNowMB;
   private double heapFreeMemoryTotalMB;
   private double heapMaxMemoryNowMB;
   private double heapMaxMemoryTotalMB;
   private double heapMemoryUsagePercent;

   private String info;

   public double getHeapUsedMemoryMB (){
      return heapUsedMemoryMB;
   }

   public void setHeapUsedMemoryMB (double heapUsedMemoryMB) {
      this.heapUsedMemoryMB = heapUsedMemoryMB;
   }

   public double getHeapFreeMemoryNowMB () {
      return heapFreeMemoryNowMB;
   }

   public void setHeapFreeMemoryNowMB (double heapFreeMemoryNowMB)
   {
      this.heapFreeMemoryNowMB = heapFreeMemoryNowMB;
   }

   public double getHeapFreeMemoryTotalMB () {
      return heapFreeMemoryTotalMB;
   }

   public void setHeapFreeMemoryTotalMB (double heapFreeMemoryTotalMB) {
      this.heapFreeMemoryTotalMB = heapFreeMemoryTotalMB;
   }

   public double getHeapMaxMemoryNowMB () {
      return heapMaxMemoryNowMB;
   }

   public void setHeapMaxMemoryNowMB (double heapMaxMemoryNowMB) {
      this.heapMaxMemoryNowMB = heapMaxMemoryNowMB;
   }

   public double getHeapMaxMemoryTotalMB () {
      return heapMaxMemoryTotalMB;
   }

   public void setHeapMaxMemoryTotalMB (double heapMaxMemoryTotalMB) {
      this.heapMaxMemoryTotalMB = heapMaxMemoryTotalMB;
   }

   public double getHeapMemoryUsagePercent () {
      return heapMemoryUsagePercent;
   }

   public void setHeapMemoryUsagePercent (double heapMemoryUsagePercent) {
      this.heapMemoryUsagePercent = heapMemoryUsagePercent;
   }

   public String getInfo () {
      return info;
   }

   public void setInfo (String info) {
      this.info = info;
   }
}
