var openDbAdminBtn;
var openSqlQueryBtn;
var openLogJournalBtn;

var DbAdminPathName = "/DbAdmin/";
var sqlQueryPathName = "";
var LogJournalPathName = "/DbLogJournal/";

window.addEventListener("load", init, false);

function init() {

    openDbAdminBtn = document.getElementById("dbAdminGui");
    openSqlQueryBtn = document.getElementById("sqlQuery");
    openLogJournalBtn = document.getElementById("dbLogJournal");

    openDbAdminBtn.addEventListener("click",()=>location.assign(location.origin + DbAdminPathName));
    openSqlQueryBtn.addEventListener("click",()=>location.assign(location.origin + sqlQueryPathName));
    openLogJournalBtn.addEventListener("click",()=>location.assign(location.origin + LogJournalPathName));
}