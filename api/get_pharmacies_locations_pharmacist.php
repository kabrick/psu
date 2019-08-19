<?php
include 'dbconfig.php';

$id = $_GET['id'];

$responce = array();

$sql = "SELECT c.latitude, c.longitude, p.location, p.pharmacy FROM psu_pharm_cordinates c, psu_pharmacies p WHERE c.pharmacy_id = p.id AND p.update_by = '$id'";

$result = $conn->query($sql);

$count = 0;

while($row = $result->fetch_assoc()) {
	$responce[$count]['latitude'] = $row['latitude'];
	$responce[$count]['longitude'] = $row['longitude'];
	$responce[$count]['location'] = $row['location'];
	$responce[$count]['pharmacy'] = $row['pharmacy'];

	$count++;
}

echo json_encode($responce);

?>