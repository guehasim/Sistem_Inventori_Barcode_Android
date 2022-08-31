<?php
	include "dbConnect.php";
	
	$sub_id		= $_POST['sub_stuff_id'];
	$sedia		= $_POST['sedia'];
	$tgl_kembali= $_POST['tgl_kembali'];

	class emp{}
	
	if (empty($sub_id)) { 
		$response = new emp();
		$response->success = 0;
		$response->message = "Kolom isian tidak boleh kosong"; 
		die(json_encode($response));
	} else {
		$querys = mysql_query("UPDATE invent_borrow_detail SET borrow_return_date='".$tgl_kembali."' WHERE sub_stuff_id='".$sub_id."' ");
		
		$query = mysql_query("UPDATE invent_sub_stuff SET sub_stuff_borrow='".$sedia."' WHERE sub_stuff_id='".$sub_id."'");
		
		if ($querys || $query) {
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