<?php
	include "dbConnect.php";
	
	$id 	= $_POST['id'];
	
	class emp{}
	 
	if (empty($id)) { 
		$response = new emp();
		$response->success = 0;
		$response->message = "Error Mengambil Data id"; 
		die(json_encode($response));
	} else {
		$query 	= mysql_query("SELECT spec_table, spec_processor, spec_ram, spec_harddisk, spec_operating_system, spec_computer_name, spec_ip_address, spec_vga_card, spec_lan_card, spec_dvd_rom, spec_monitor, spec_keyboard, spec_mouse
								FROM invent_sub_stuff_spec WHERE sub_stuff_id ='".$id."' ");
		$row 	= mysql_fetch_array($query);
		
		if (!empty($row)) {
			$response 							= new emp();
			$response->success					= 1;
			$response->spec_table 				= $row["spec_table"];
			$response->spec_processor 			= $row["spec_processor"];
			$response->spec_ram 				= $row["spec_ram"];
			$response->spec_harddisk 			= $row["spec_harddisk"];
			$response->spec_operating_system 	= $row["spec_operating_system"];
			$response->spec_computer_name 		= $row["spec_computer_name"];
			$response->spec_ip_address 			= $row["spec_ip_address"];
			$response->spec_vga_card 			= $row["spec_vga_card"];
			$response->spec_lan_card 			= $row["spec_lan_card"];
			$response->spec_dvd_rom 			= $row["spec_dvd_rom"];
			$response->spec_monitor 			= $row["spec_monitor"];
			$response->spec_keyboard 			= $row["spec_keyboard"];
			$response->spec_mouse 				= $row["spec_mouse"];
			die(json_encode($response));
		} else{ 
			$response = new emp();
			$response->success = 0;
			$response->message = "Error Mengambil Data";
			die(json_encode($response)); 
		}	
	}
?>