# WebOfEscope

Web Of Escope   
基于:Escope  
https://github.com/YeliangQiu/Escope  
使用Spring Boot框架
 
### 使用时需要修改的部分
1. WSN
   1. WSNMain类  
      &emsp;PORT
   2. WSNSQLcoonection类  
      &emsp;url  
      &emsp;user  
      &emsp;passwd
2. escope

### 数据库(暂定)
机柜									
									
	编号	位置		(可以用来表示楼层)					
	id	x	y	z					
	int	float	float	float/楼层的话int					
									
服务器	ServerStatus								
									
	服务器信息	ServerInfo							
	ip	username	password	所属机柜编号	机柜中所在层	高度			
	ip	username	password	cabinet_id	layer	height			
	char()	char()	char()	int	int	int			
									
	服务器状态	Record							
	ip	值A	值B	值C	…	时间			
	ip	A	B	C	…	time			
	char()	float/int	float/int	float/int	…	datetime			
		CPU_id	total_Mem	avail_Mem	CPU_avg_Temp	VIN	IIN	Total_Power	CPU_Watts
		空闲CPU时间	总内存	可用内存	CPU平均温度	输入电压	输入电流	总功耗	CPU功率
									
传感器	WirelessSensorRecord								
									
	传感器信息	sensor							
	地址	位置	(可能位置依附于服务器，就不用坐标)						
	address	x	y	z					
	char()	float	float	float					
									
	多个表，每个表对应一种传感器数据。								
	湿度	温度	光照	烟雾(可燃气体)	红外	CO2			
	humidity	temperature	light	smog	ultra	CO2			
	地址	传感器的值	时间						
	address	value	time						
	char()	float/int	datetime						

