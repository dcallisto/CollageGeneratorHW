// data_classes.js
// Team 5
// Version 1.0

// Class representing an individual collage
// Contains the image and the topic, as well as a number for sorting
class Collage {
	constructor(number, topic, image) {
		this.number = number;
		this.topic = topic;
		this.image = image;
	}
}

// Class representing the full list of generated collages
// Contains an array of Collage objects
// Contains convenience methods for getting previous collages and
// displaying collages.
class CollageList {
	// Simply initializes with empty values
	constructor() {
		this.collages = [];
		this.currentCollage = 0;
	}

	// Adds a new collage to the collage list.
	// Pass in a Collage object.
	addCollage(collage) {
		this.collages.push(collage);
		this.currentCollage = collages.length;
	}

	// Returns an array of previous collages.
	// Excludes the currently-displayed collage.
	getPrevCollages() {
		var collages = [];
		for (var i = 0; i < collages.length; ++i) {
			var collage = collages[i];
			if (collage.number != currentCollage) {
				collages.push(collage);
			}
		}
		return collages;
	}

	// Returns the collage with the specified collage number.
	getCollage(collageNum) {
		return collages[collageNum - 1];
	}

	// Returns the previous collage with the specified collage number.
	getPrevCollage(collageNum) {
		var prevCollages = this.getPrevCollages();
		for (var i = 0; i < prevCollages.length; ++i) {
			var prevCollage = prevCollages[i];
			if (prevCollage.number == collageNum) {
				this.currentCollage = collageNum;
				return prevCollage;
			}
		}
	}
}
