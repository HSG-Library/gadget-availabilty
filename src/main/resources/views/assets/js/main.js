document.addEventListener('DOMContentLoaded', function () {
	gadgets.init()
})

gadgets = {
	init: function () {
		console.log('init gadgets')
		this.loadGadgets()
		this.registerSearchToggle()
		this.registerDescriptionToggle()
	},

	loadGadgets: function () {
		if (util.byClass('.js-load-gadgets').length > 0) {
			util.get('/gadgets/all', this.onLoadGadgets, this.onLoadGadgetsError)
		}
	},

	onLoadGadgets: function () {
		if (this.status >= 200 && this.status < 400) {
			const list = util.byClass('.js-items').item(0)
			const data = JSON.parse(this.response)
			let itemsMarkup = ''
			let searchIndex = []
			let totalItems = 0
			let totalAvailable = 0
			data.forEach((gadget, idx) => {
				itemsMarkup += item.getMarkup(
					gadget.id,
					idx,
					gadget.title,
					gadget.img_id,
					(gadget.description || ''),
					gadget.available,
					gadget.total,
					gadget.details,
				)
				totalAvailable += gadget.available
				totalItems += gadget.total
				searchIndex.push(
					{
						id: gadget.id,
						txt: gadget.title.toLowerCase() + ' ' + (gadget.description || '').toLowerCase()
					}
				)
			})
			console.log("total gadgets:", totalItems)
			console.log("total available:", totalAvailable)
			list.innerHTML = itemsMarkup
			gadgets.initSearch(searchIndex)
		} else {
			gadgets.onLoadGadgetsError()
		}
		util.hideLoader()
	},

	onLoadGadgetsError: function () {
		console.error('Could not load gadgets')
		util.hideLoader()
		util.byId('js-error').classList.remove('hide')
	},

	registerSearchToggle: function () {
		const searchButton = util.byId('js-search-button')
		if (!searchButton) {
			return
		}
		searchButton.addEventListener('click', function () {
			const input = util.byId('js-search-input')
			input.classList.toggle('active')
			if (input.classList.contains('active')) {
				input.focus()
			} else {
				input.blur()
			}
		})
	},

	registerDescriptionToggle: function () {
		document.addEventListener('click', event => {
			if (event.target.closest('.tile')) {
				const tile = event.target.closest('.tile')
				if (event.altKey) {
					const details = tile.querySelectorAll('.js-details')
					details.forEach((el) => { el.classList.toggle('hide') })
				} else {
					const desc = tile.querySelectorAll('.js-description')
					desc.forEach((el) => { el.classList.toggle('active') })
				}
			}
		})
	},

	initSearch: function (searchIndex) {
		const input = util.byId('js-search-input')
		input.addEventListener('keyup', () => {
			const term = input.value.toLowerCase()
			if (term.length >= 1) {
				gadgets.search(term, searchIndex)
			} else {
				util.byClass('.js-tile').forEach((tile) => tile.classList.remove('hide'))
			}
		})
	},

	search: function (term, searchIndex) {
		util.byClass('.js-tile').forEach((tile) => {
			tile.classList.remove('initial')
			tile.classList.add('hide')
		})
		searchIndex.forEach((entry) => {
			if (entry.txt.indexOf(term) >= 0) {
				util.byId(entry.id).classList.remove('hide')
			}
		})
	}
}

item = {
	getMarkup: function (id, delay, title, img, description, available, total, details) {
		let availableClass = 'available'
		if (available === 0) {
			availableClass = 'notAvailable'
		}
		const delayMs = Math.floor((delay / 3) * 100)
		return '<li class="tile initial js-tile ' + availableClass + '" id="' + id + '" style="animation-delay: ' + delayMs + 'ms; ">' +
			'<div class="info js-info"><h2>' + title + '</h2></div>' +
			'<div class="container" style="background-image: url(assets/img/' + img + '), url(assets/img/unavailable.jpg);;">' +
			'<pre class="details js-details hide">' + JSON.stringify(details, null, 2) + '</pre>' +
			'<p class="description js-description"><span class="description-title">' + title + '</span><br>' + description + '</p>' +
			'<p class="availability">' + available + ' / ' + total + '</p>' +
			'</div>' +
			'</li >'
	}
}


util = {
	byId: function (id) {
		return document.getElementById(id)
	},

	byClass: function (className) {
		return document.querySelectorAll(className)
	},

	hideLoader: function () {
		util.byId('js-loader').classList.add('hide')
	},

	get: function (uri, success, error) {
		const request = new XMLHttpRequest()
		request.open('GET', uri, true)
		request.onload = success
		request.onerrorm = error
		request.send()
	}
}
