<?php

include 'dbconfig.php';

$originalImgName = $_FILES['filename']['name'];
$tempName = $_FILES['filename']['tmp_name'];
$folder = "images/";
$url = "images/".$originalImgName;
$psu_id = $_POST['psu_id'];

if(move_uploaded_file($tempName,$folder.$originalImgName)){
	
	$query = "UPDATE psu_admin SET photo='$url' WHERE psu_id = '$psu_id'";

	if($conn->query($query) === TRUE){
		echo "";
	}else{
		echo "";
	}
}else{
	echo "";
}

?>