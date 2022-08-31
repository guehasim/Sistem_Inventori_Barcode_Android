<?php
	include "dbConnect.php";
	
	$sub_id 	= $_POST['sub_stuff_id'];
	$date 		= $_POST['log_spec_date']; 
	$table 		= $_POST['spec_table'];
	$processor 	= $_POST['spec_processor'];
	$ram 		= $_POST['spec_ram'];
	$hardisk 	= $_POST['spec_harddisk'];
	$so 		= $_POST['spec_operating_system'];
	$pc 		= $_POST['spec_computer_name'];
	$ip 		= $_POST['spec_ip_address'];
	$vga 		= $_POST['spec_vga_card'];
	$lan 		= $_POST['spec_lan_card'];
	$dvd 		= $_POST['spec_dvd_rom'];
	$monitor 	= $_POST['spec_monitor'];
	$keyboard 	= $_POST['spec_keyboard'];
	$mouse 		= $_POST['spec_mouse']; 
//Creating an sql query 
	
	class emp{}
	
	if (empty($sub_id) || empty($table) || empty($date)) { 
		$response = new emp();
		$response->success = 0;
		$response->message = "Kolom isian tidak boleh kosong"; 
		die(json_encode($response));
	} else {

		$query 	= mysql_query("INSERT INTO invent_sub_stuff_spec(spec_id, sub_stuff_id, spec_table, spec_processor, spec_ram, spec_harddisk, spec_operating_system,
							  spec_computer_name, spec_ip_address, spec_vga_card, spec_lan_card, spec_dvd_rom, spec_monitor, spec_keyboard, spec_mouse)
							  VALUES (0,'".$sub_id."','".$table."','".$processor."','".$ram."','".$hardisk."','".$so."','".$pc."','".$ip."','".$vga."','".$lan."',
							  	'".$dvd."','".$monitor."','".$keyboard."','".$mouse."')");


		$querys	= mysql_query("INSERT INTO invent_sub_stuff_log_spec(log_spec_id, sub_stuff_id, log_spec_date, log_spec_table, log_spec_processor, log_spec_ram,
								log_spec_harddisk, log_spec_operating_system, log_spec_computer_name, log_spec_ip_address, log_spec_vga_card, log_spec_lan_card,
								log_spec_dvd_rom, log_spec_monitor, log_spec_keyboard, log_spec_mouse)
								VALUES (0,'".$sub_id."','".$date."','".$table."','".$processor."','".$ram."','".$hardisk."','".$so."','".$pc."','".$ip."','".$vga."','".$lan."',
							  	'".$dvd."','".$monitor."','".$keyboard."','".$mouse."')");

		
		if ($query || $querys) {
			$response = new emp();
			$response->success = 1;
			$response->message = "Data berhasil di simpan";
			die(json_encode($response));
		} else{ 
			$response = new emp();
			$response->success = 0;
			$response->message = "Error simpan Data";
			die(json_encode($response)); 
		}	
	}
?>

