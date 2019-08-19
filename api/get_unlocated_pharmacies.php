<?php
include 'dbconfig.php';

$id = $_GET['id'];

$sql = "SELECT id, pharmacy FROM psu_pharmacies WHERE update_by = '$id' AND location_set = 0 ORDER BY pharmacy";

$result = $conn->query($sql);

$responce = array();

$count = 0;

while($row = $result->fetch_assoc()) {
	$responce[$count]['pharmacy_name'] = $row['pharmacy'];
	$responce[$count]['pharmacy_id'] = $row['id'];

	$count++;
}

echo json_encode($responce);

?>