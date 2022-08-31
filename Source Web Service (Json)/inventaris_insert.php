<?php
	include "dbConnect.php";
	
	$stuff_name = $_POST['stuff_name']; 
	$stuff_brand = $_POST['stuff_brand']; 
	$stuff_model = $_POST['stuff_model'];

	$parent_id = $_POST['parent_id'];
	$sub_stuff_serial_number = $_POST['sub_stuff_serial_number'];
	$sub_stuff_condition = $_POST['sub_stuff_condition'];
	$sub_stuff_borrow = $_POST['sub_stuff_borrow'];
	$sub_stuff_year_purchase = $_POST['sub_stuff_year_purchase']; 
//Creating an sql query 
	
	class emp{}
	
	if (empty($stuff_name) || empty($sub_stuff_serial_number) || empty($sub_stuff_year_purchase) || empty($parent_id)) { 
		$response = new emp();
		$response->success = 0;
		$response->message = "Kolom isian tidak boleh kosong"; 
		die(json_encode($response));
	} else {

		$query = mysql_query("INSERT INTO invent_stuff(stuff_id,stuff_name, stuff_brand, stuff_model) VALUES (0,'".$stuff_name."','".$stuff_brand."','".$stuff_model."')");

		$stuff_id = mysql_insert_id();

		$querys = mysql_query("INSERT INTO invent_sub_stuff (sub_stuff_id,stuff_id, parent_id, sub_stuff_serial_number, sub_stuff_condition, sub_stuff_borrow, sub_stuff_year_purchase) 
		VALUES (0, '".$stuff_id."','".$parent_id."','".$sub_stuff_serial_number."','".$sub_stuff_condition."','".$sub_stuff_borrow."','".$sub_stuff_year_purchase."')");
		
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

