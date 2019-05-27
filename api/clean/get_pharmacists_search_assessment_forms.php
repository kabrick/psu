<?php
include 'dbconfig.php';

$sql = "SELECT DISTINCT p.pharmacist_id as id, a.name FROM psu_admin a, psu_pharmacist_assessment p WHERE a.psu_id = p.pharmacist_id";

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