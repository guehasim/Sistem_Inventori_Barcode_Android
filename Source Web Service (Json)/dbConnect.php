<?php
	$server		= "mysql.serversfree.com"; // sesuaikan alamat server anda
	$user		= "u976252743_ilab"; // sesuaikan user web server anda
	$password	= "hasim8"; // sesuaikan password web server anda
	$database	= "u976252743_ilab"; // sesuaikan database web server anda
	
	$connect = mysql_connect($server, $user, $password) or die ("Koneksi gagal!");
	mysql_select_db($database) or die ("Database belum siap!");
?>