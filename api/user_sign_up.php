<?php

include 'dbconfig.php';

$originalImgName = $_FILES['filename']['name'];
$tempName = $_FILES['filename']['tmp_name'];
$folder = "images/";
$url = "images/".$originalImgName;
$full_names = $_POST['full_names'];
$email = $_POST['email'];
$phone = $_POST['phone'];
$mem_status = $_POST['mem_status'];
$password = $_POST['password'];
$username = $_POST['username'];

// check if email exists
$sql_e = "SELECT name FROM psu_admin WHERE email='$email'";
$result_e = $conn->query($sql_e);

if($result_e->num_rows > 0){
	echo "2";
	exit();
}

// check if username exists
$sql_u = "SELECT name FROM psu_admin WHERE username='$username'";
$result_u = $conn->query($sql_u);

if($result_u->num_rows > 0){
	echo "3";
	exit();
}

// check if phone exists
$sql_p = "SELECT name FROM psu_admin WHERE phone='$phone'";
$result_p = $conn->query($sql_p);

if($result_p->num_rows > 0){
	echo "4";
	exit();
}

if(move_uploaded_file($tempName,$folder.$originalImgName)){

	//get the last id
	$sql_id = "SELECT psu_id FROM psu_admin ORDER BY psu_id DESC LIMIT 1";

	$result_id = $conn->query($sql_id);
	$result_assoc_id = $result_id->fetch_assoc();
	$psu_id = $result_assoc_id['psu_id'];

	$psu_id = $psu_id + 1;

	$date = date("Y-m-d");

	$password = base64_encode($password);

	//insert values
	$sql = "INSERT INTO psu_admin (psu_id, name, email, phone, username, password, type, status, date_created, photo) VALUES ('$psu_id','$full_names','$email','$phone','$username','$password','$mem_status','active','$date', '$url')";

	if ($conn->query($sql) === TRUE) {
	    echo "1";
	} else {
	    echo "0";
	}
}else{
	echo "0";
}

?>