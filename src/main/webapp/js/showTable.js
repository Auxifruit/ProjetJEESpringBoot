function toggleTable() {
    const table = document.getElementById("existingTable");
    if (table.style.display === "none" || table.style.display === "") {
        table.style.display = "table";
    } else {
        table.style.display = "none";
    }
}
