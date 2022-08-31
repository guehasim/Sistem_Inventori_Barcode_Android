<?php
	include "dbConnect.php";
	
	$peminjam	= $_POST['borrow_borrower'];
	$tgl_pinjam	= $_POST['borrow_date'];
	$note 		= $_POST['borrow_note'];
	$sub_id 	= $_POST['sub_stuff_id'];
//Creating an sql query 
	
	class emp{}
	
	if (empty($peminjam)) { 
		$response = new emp();
		$response->success = 0;
		$response->message = "Kolom isian tidak boleh kosong"; 
		die(json_encode($response));
	} else {

		$query = mysql_query("INSERT INTO invent_borrow(borrow_id,borrow_borrower,borrow_date,borrow_note) VALUES (0,'".$peminjam."','".$tgl_pinjam."','".$note."')");

		$borrow_id = mysql_insert_id();

		$querys = mysql_query("INSERT INTO invent_borrow_detail (borrow_id,sub_stuff_id,borrow_return_date)	VALUES ('".$borrow_id."','".$sub_id."',NULL)");

		
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

