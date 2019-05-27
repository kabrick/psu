<?php

include 'dbconfig.php';

$originalImgName = $_FILES['filename']['name'];
$tempName = $_FILES['filename']['tmp_name'];
$folder = "images/";
$url = "images/".$originalImgName;
$id = $_POST['id'];
$psu_id = $_POST['psu_id'];
$cover_letter = $_POST['cover_letter'];

// check if user has already applied for this job

$sql = "SELECT id FROM psu_jobs_applications WHERE psu_id = '$psu_id' AND jobs_id = '$id'";

$result = $conn->query($sql);

if ($result->num_rows > 0){
	echo "2";
} else {
	if(move_uploaded_file($tempName,$folder.$originalImgName)){
		$sql = "INSERT INTO psu_jobs_applications (psu_id, jobs_id, cover_letter, resume) VALUES ('$psu_id','$id', '$cover_letter', '$url')";

		if ($conn->query($sql) === TRUE) {
		    echo "1";
		} else {
		    echo "0";
		}
	} else {
		echo "0";
	}
}

?>