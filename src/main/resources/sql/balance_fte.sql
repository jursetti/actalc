SELECT 'FTE', SUM(cloud_actadm.statistic_amount) as "ACC"
FROM cloud_actadm
WHERE ( (cloud_actadm.account like '951%') AND (cloud_actadm.business_unit NOT LIKE 'G8%') AND (cloud_actadm.business_unit NOT LIKE 'E8%')) group by 'FTE'
