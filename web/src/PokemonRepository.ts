
export default interface PokemonRepository {
	getPokemonNamesByOffset(offset: number): Promise<string[]>
}

export class NetworkPokemonRepository implements PokemonRepository {
	async getPokemonNamesByOffset(offset: number): Promise<string[]> {
		const res = await fetch(`https://pokeapi.co/api/v2/pokemon?offset=${offset}`)
		const json = await res.json()
		return json.results.map((it: {name: string}) => it.name)
	}
}

export const getPokemonNamesByOffset = async (offset: number): Promise<string[]> => {
	const res = await fetch(`https://pokeapi.co/api/v2/pokemon?offset=${offset}`)
	const json = await res.json()
	return json.results.map((it: {name: string}) => it.name)
}