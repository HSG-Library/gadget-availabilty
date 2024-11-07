document.addEventListener('DOMContentLoaded', function () {
	gadgets.init()
})

gadgets = {
	init: function () {
		console.log('init gadgets')
		console.log('lang:', this.lang())
		this.onLoad()
		this.registerDescriptionToggle()
	},

	onLoad: function () {
		const list = util.byClass('.js-items').item(0)
		const items = util.byClass('.js-tile');
		const searchIndex = [];
		items.forEach((tile, idx) => {
			tile.style.animationDelay = (idx * 30) + 'ms'
			const title = tile.querySelector('.js-title').textContent.toLowerCase();
			const description = tile.querySelector('.js-description-text').textContent.toLowerCase();
			const txt = title + ' ' + description;
			searchIndex.push({
				id: tile.id,
				txt: txt
			})
		})
		gadgets.initSearch(searchIndex)
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
	},

	lang: function () {
		return document.documentElement.lang
	}
}

util = {
	byId: function (id) {
		return document.getElementById(id)
	},

	byClass: function (className) {
		return document.querySelectorAll(className)
	},

	get: function (uri, success, error) {
		const request = new XMLHttpRequest()
		request.open('GET', uri, true)
		request.onload = success
		request.onerrorm = error
		request.send()
	}
}
