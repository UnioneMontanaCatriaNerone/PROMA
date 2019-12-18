cd C:\Program Files\GeoKettle
call kitchen.bat /file:C:\ETL\job_catasto_e_mappe\Job-Catasto_grafico.kjb /level: Detailed>C:/ETL/log/update_grafico.log
call kitchen.bat /file:C:\ETL\job_catasto_e_mappe\Job-Catasto_censuario.kjb /level: Detailed>C:/ETL/log/update_censuario.log

