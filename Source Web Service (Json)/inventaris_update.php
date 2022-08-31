<?php
	include "dbConnect.php";
	
	$id 		= $_POST['stuff_id'];
	$nama		= $_POST['stuff_name'];
	$brand		= $_POST['stuff_brand'];
	$model		= $_POST['stuff_model'];
	$serial		= $_POST['sub_stuff_serial_number'];
	$kondisi	= $_POST['sub_stuff_condition'];
	$sedia		= $_POST['sub_stuff_borrow'];
	$tahun		= $_POST['sub_stuff_year_purchase'];

	class emp{}
	
	if (empty($id)) { 
		$response = new emp();
		$response->success = 0;
		$response->message = "Kolom isian tidak boleh kosong"; 
		die(json_encode($response));
	} else {
		$query = mysql_query("UPDATE invent_stuff SET stuff_name='".$nama."', stuff_brand='".$brand."', stuff_model='".$model."' WHERE stuff_id='".$id."'");
		$query = mysql_query("UPDATE invent_sub_stuff SET sub_stuff_serial_number='".$serial."', sub_stuff_condition='".$kondisi."', sub_stuff_borrow='".$sedia."', sub_stuff_year_purchase='".$tahun."' WHERE stuff_id='".$id."' ");
		
		if ($query) {
			$response = new emp();
			$response->success = 1;
			$response->message = "Data berhasil di update";
			die(json_encode($response));
		} else{ 
			$response = new emp();
			$response->success = 0;
			$response->message = "Error update Data";
			die(json_encode($response)); 
		}	
	}
?>