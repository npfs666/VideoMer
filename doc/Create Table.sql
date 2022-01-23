CREATE TABLE EV
(
	id INT PRIMARY KEY NOT NULL,
    
	acteurs VARCHAR(100),
	affichette VARCHAR(255),
	categorie INT,
	date VARCHAR(50),
	datePret VARCHAR(50),
	emplacement VARCHAR(100),
	genre VARCHAR(100),
	infosUtilisateur VARCHAR(255),
	nom VARCHAR(100),
	pays VARCHAR(100),
	personnePret VARCHAR(100),
	realisateurs VARCHAR(100),
	support INT,
	synopsis VARCHAR(500),
	originalName VARCHAR(100),
	playtime VARCHAR(100),
	
	invites VARCHAR(100),
	noEpisode INT,
	nomEpisode VARCHAR(100),
	noSaison INT,
	
	numSupport INT
)

CREATE TABLE `EV` (
	`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	`acteurs`	VARCHAR(100),
	`affichette`	VARCHAR(255),
	`categorie`	INT,
	`date`	VARCHAR(50),
	`datePret`	VARCHAR(50),
	`emplacement`	VARCHAR(100),
	`genre`	VARCHAR(100),
	`infosUtilisateur`	VARCHAR(255),
	`nom`	VARCHAR(100),
	`pays`	VARCHAR(100),
	`personnePret`	VARCHAR(100),
	`realisateurs`	VARCHAR(100),
	`support`	INT,
	`synopsis`	VARCHAR(500),
	`originalName`	VARCHAR(100),
	`playtime`	VARCHAR(100),
	`invites`	VARCHAR(100),
	`noEpisode`	INT,
	`nomEpisode`	VARCHAR(100),
	`noSaison`	INT,
	`numSupport`	INT
);


INSERT INTO EV (acteurs, affichette, categorie, date, datePret, emplacement, genre, infosUtilisateur, nom, pays, personnePret, realisateurs, support, synopsis, originalName, playtime, invites, noEpisode, nomEpisode, noSaison, numSupport) VALUES 
(
\""+ev.getActeurs().toString().replaceAll("[\\[\\]]", "")+"\",
\""+ev.getAffichette()+"\",
\""+ev.getCategorie()+"\",
\""+ev.getDate()+"\",
\""+ev.getDatePret()+"\",
\""+ev.getEmplacement()+"\",
\""+ev.getGenre().toString().replaceAll("[\\[\\]]", "")+"\",
\""+ev.getInfosUtilisateur()+"\",
\""+ev.getNom()+"\",
\""+ev.getPays().toString().replaceAll("[\\[\\]]", "")+"\",
\""+ev.getPersonnePret()+"\",
\""+ev.getRealisateurs().toString().replaceAll("[\\[\\]]", "")+"\",
\""+ev.getSupport()+"\",
\""+ev.getSynopsis()+"\",
\""+ev.getOriginalName()+"\",
\""+ev.getPlaytime()+"\",

\""+((Serie)ev).getInvites().toString().replaceAll("[\\[\\]]", "")+"\",
\""+((Serie)ev).getNoEpisode()+"\",
\""+((Serie)ev).getNomEpisode()+"\",
\""+((Serie)ev).getNoSaison()+"\",

\"" + ((Film) ev).getNumSupport() + "\"



UPDATE EV SET acteurs = \""+ev.getActeurs().toString().replaceAll("[\\[\\]]", "")+"\", affichette = \""+ev.getAffichette()+"\", categorie = \""+ev.getCategorie()+"\", date = \""+ev.getDate()+"\",
	datePret = \""+ev.getDatePret()+"\", emplacement = \""+ev.getEmplacement()+"\", genre = \""+ev.getGenre().toString().replaceAll("[\\[\\]]", "")+"\", infosUtilisateur = \""+ev.getInfosUtilisateur()+"\",
	nom = \""+ev.getNom()+"\", pays = \""+ev.getPays().toString().replaceAll("[\\[\\]]", "")+"\", personnePret = \""+ev.getPersonnePret()+"\", realisateurs = \""+ev.getRealisateurs().toString().replaceAll("[\\[\\]]", "")+"\",
	support = \""+ev.getSupport()+"\", synopsis = \""+ev.getSynopsis()+"\", originalName = \""+ev.getOriginalName()+"\", playtime = \""+ev.getPlaytime()+"\",
	invites = \""+((Serie)ev).getInvites().toString().replaceAll("[\\[\\]]", "")+"\", noEpisode = \""+((Serie)ev).getNoEpisode()+"\", nomEpisode = \""+((Serie)ev).getNomEpisode()+"\",
	noSaison = \""+((Serie)ev).getNoSaison()+"\", numSupport = \"" + ((Film) ev).getNumSupport() + "\"
	
	



CREATE TABLE PREF 
(
	id INT PRIMARY KEY NOT NULL,
	
	lookAndFeel VARCHAR(100),
	relatif INT,
	surveillerDemarrage INT,
	videoExtensions VARCHAR(100),
	regexFileCleanSpace VARCHAR(100),
	regexFileCleanBlank VARCHAR(100),
	regexMovie VARCHAR(500),
	regexTvShow VARCHAR(500),
	lang VARCHAR(20),
	playList VARCHAR(100),
	currentVideoTime VARCHAR(20),
	frameDimension VARCHAR(10),
	frameLocation VARCHAR(10),
	windowPlaylistShowed INT,
	windowDetailsShowed INT,
	volume INT,
	moviePath VARCHAR(100),
	tvShowPath VARCHAR(100)
)

CREATE TABLE `PREF` (
	`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,
	`lookAndFeel`	VARCHAR(100),
	`relatif`	INT,
	`surveillerDemarrage`	INT,
	`videoExtensions`	VARCHAR(100),
	`regexFileCleanSpace`	VARCHAR(100),
	`regexFileCleanBlank`	VARCHAR(100),
	`regexMovie`	VARCHAR(500),
	`regexTvShow`	VARCHAR(500),
	`lang`	VARCHAR(20),
	`playList`	VARCHAR(100),
	`currentVideoTime`	VARCHAR(20),
	`frameDimension`	VARCHAR(10),
	`frameLocation`	VARCHAR(10),
	`windowPlaylistShowed`	INT,
	`windowDetailsShowed`	INT,
	`volume`	INT,
	`moviePath`	VARCHAR(100),
	`tvShowPath`	VARCHAR(100)
);


INSERT INTO EV (lookAndFeel, relatif, surveillerDemarrage, videoExtensions, regexFileCleanSpace, regexFileCleanBlank, regexMovie, regexTvShow, lang, playlist, currentVideoTime, frameDimension, frameLocation, windowPlaylistShowed, windowDetailsShowed, volume, moviePath, tvShowPath) VALUES
(
\""+p.getLookAndFeel()+"\",
\""+p.isRelatif()+"\",
\""+p.isSurveillerDemarrage()+"\",
\""+p.getVideoExtensions().toString.replaceAll()+"\",
\""+p.getRegexFileCleanSpace()+"\",
\""+p.getRegexFileCleanBlank()+"\",
\""+p.getRegexMovie()+"\",
\""+p.getRegexTvShow()+"\",
\""+p.getLang()+"\",
\""+p.getPlayList()+"\",
\""+p.getCurrentVideoTime()+"\",
\""+p.getFrameDimension()+"\",
\""+p.getFrameLocation()+"\",
\""+p.isWindowPlaylistShowed()+"\",
\""+p.isWindowDetailsShowed()+"\",
\""+p.getVolume()+"\",
\""+p.getMoviePath()+"\",
\""+p.getTvShowPath()+"\"
)

UPDATE PREF SET lookAndFeel = \""+p.getLookAndFeel()+"\", relatif=\""+p.isRelatif()+"\", surveillerDemarrage=\""+p.isSurveillerDemarrage()+"\", videoExtensions=\""+p.getVideoExtensions().toString.replaceAll()+"\",
 regexFileCleanSpace=\""+p.getRegexFileCleanSpace()+"\", regexFileCleanBlank=\""+p.getRegexFileCleanBlank()+"\", regexMovie=\""+p.getRegexMovie()+"\", regexTvShow=\""+p.getRegexTvShow()+"\",
 lang=\""+p.getLang()+"\", playlist=\""+p.getPlayList()+"\", currentVideoTime=\""+p.getCurrentVideoTime()+"\", frameDimension=\""+p.getFrameDimension()+"\",
 frameLocation=\""+p.getFrameLocation()+"\", windowPlaylistShowed=\""+p.isWindowPlaylistShowed()+"\", windowDetailsShowed=\""+p.isWindowDetailsShowed()+"\",
 volume=\""+p.getVolume()+"\", moviePath=\""+p.getMoviePath()+"\", tvShowPath=\""+p.getTvShowPath()+"\"