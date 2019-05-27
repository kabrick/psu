<?php
include 'dbconfig.php';

$sql = "SELECT id, pharmacy as name, location, status_image, location_set FROM psu_pharmacies ORDER BY pharmacy";

$result = $conn->query($sql);

if ($result->num_rows > 0) {
	while($row[] = $result->fetch_assoc()) {
 		$json = json_encode($row);
 	}
} else {
	//
}

echo $json;

?>