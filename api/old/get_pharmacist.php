<?php

include 'dbconfig.php';

$id = $_GET['id'];

$sql1 = "SELECT pharmacy_id FROM psu_pharm_directors WHERE psu_id = '$id'";

$result1 = $conn->query($sql1);

$row1 = $result1->fetch_assoc();

$pharmacy_id = $row1['pharmacy_id'];

$sql = "SELECT update_by FROM psu_pharmacies WHERE id = '$pharmacy_id'";

$result = $conn->query($sql);

$row = $result->fetch_assoc();

if($row['update_by']){
	echo $row['update_by'] . "-" . $pharmacy_id;
} else {
	echo "0";
}

?>