document.addEventListener("DOMContentLoaded", function () {
	gadgets.init();
});

gadgets = {
	init: function () {
		console.log("init gadgets");
		this.loadGadgets();
		this.registerSearchToggle();
		this.registerSearch();
		this.registerDescriptionToggle();
	},

	loadGadgets: function () {
		util.get("/gadgets/all", this.onLoadGadgets, this.onLoadGadgetsError);
	},

	onLoadGadgets: function () {
		if (this.status >= 200 && this.status < 400) {
			const list = util.byClass('.js-items').item(0);
			const data = JSON.parse(this.response);
			let itemsMarkup = "";
			let searchIndex = [];
			let totalItems = 0;
			let totalAvailable = 0;
			data.forEach((gadget, idx) => {
				itemsMarkup += item.getMarkup(
					gadget.volume,
					idx,
					gadget.callno,
					gadget.img_id,
					gadget.note,
					gadget.available,
					gadget.total
				);
				totalAvailable += gadget.available;
				totalItems += gadget.total;
				searchIndex.push(
					{
						id: gadget.volume,
						txt: gadget.callno.toLowerCase() + " " + gadget.note.toLowerCase()
					}
				);
			});
			list.innerHTML = itemsMarkup;
			gadgets.initSearch(searchIndex);
			const title = util.byId("js-title");
			title.innerHTML = title.innerHTML + " (" + totalAvailable + "&nbsp;&#47;&nbsp;" + totalItems + ")";
		} else {
			gadgets.onLoadGadgetsError();
		}
		util.hideLoader();
	},

	onLoadGadgetsError: function () {
		console.error("Could not load gadgets");
		util.hideLoader();
		util.byId("js-error").classList.remove('hide');
	},

	registerSearchToggle: function () {
		const searchButton = util.byId("js-search-button");
		searchButton.addEventListener("click", function () {
			const input = util.byId("js-search-input");
			input.classList.toggle("active");
			input.focus();
		});
	},

	registerSearch: function () {
		console.log("filter items");
	},

	registerDescriptionToggle: function () {
		document.addEventListener("click", event => {
			if (event.target.closest(".tile")) {
				const tile = event.target.closest(".tile");
				const desc = tile.querySelectorAll(".description");
				desc.forEach((el) => { el.classList.toggle("active"); })
			}
		});
	},

	initSearch: function (searchIndex) {
		const input = util.byId("js-search-input");
		input.addEventListener("keyup", () => {
			const term = input.value.toLowerCase();
			if (term.length >= 1) {
				gadgets.search(term, searchIndex);
			} else {
				util.byClass('.js-tile').forEach((tile) => tile.classList.remove("hide"));
			}
		});
	},

	search: function (term, searchIndex) {
		util.byClass('.js-tile').forEach((tile) => {
			tile.classList.remove("initial");
			tile.classList.add("hide");
		});
		searchIndex.forEach((entry) => {
			if (entry.txt.indexOf(term) >= 0) {
				util.byId(entry.id).classList.remove("hide");
			}
		});
	}
};

item = {
	getMarkup: function (id, delay, title, img, description, available, total) {
		let availableClass = "available";
		if (available == 0) {
			availableClass = "notAvailable";
		}
		const delayMs = Math.floor((delay / 3) * 100);
		const markup = '<li class="tile initial js-tile ' + availableClass + '" id="' + id + '" style="animation-delay: ' + delayMs + 'ms; ">' +
			'<div class="info"><h2>' + title + '</h2></div>' +
			'<div class="container" style="background-image: url(assets/img/' + img + ');">' +
			'<p class="description">' + title + '<br>' + description + '</p>' +
			'<p class="availability">' + available + ' / ' + total + '</p>' +
			'</div>' +
			'</li >'
		return markup;
	}
}


util = {
	byId: function (id) {
		return document.getElementById(id);
	},

	byClass: function (className) {
		return document.querySelectorAll(className);
	},

	hideLoader: function () {
		util.byId('js-loader').classList.add('hide');
	},

	get: function (uri, success, error) {
		const request = new XMLHttpRequest();
		request.open("GET", uri, true);
		request.onload = success;
		request.onerrorm = error;
		request.send();
	}
};
