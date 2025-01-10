import { createContext, ReactNode, useContext, useEffect, useMemo, useState } from 'react'

type PokemonContextReadOnly = {
	readonly pokemonNames: string[]
	readonly currentPage: number
}

type PokemonContextWriteOnly = {
	readonly fetchNext: () => void
	readonly fetchPrev: () => void
}

const fetchPokemonsByOffset = async (offset: number): Promise<string[]> => {
	const res = await fetch(`https://pokeapi.co/api/v2/pokemon?offset=${offset}`)
	const json = await res.json()
	return json.results.map((it: {name: string}) => it.name)
}

const PokemonContextReadOnly = createContext<PokemonContextReadOnly>(undefined as never)
const PokemonContextWriteOnly = createContext<PokemonContextWriteOnly>(undefined as never)

const usePokemonContextReadOnly = () => useContext(PokemonContextReadOnly)
const usePokemonContextWriteOnly = () => useContext(PokemonContextWriteOnly)

export function PokemonContextProvider({children}: {children: ReactNode}) {
	const [pokemonNames, setPokemonNames] = useState<string[]>([])
	const [currentPage, setCurrentPage] = useState(0)


	useEffect(() => {
		fetchPokemonsByOffset(currentPage * 20)
			.then(pokemonNames => setPokemonNames(pokemonNames))
	}, [currentPage])

	const pokemonContextWriteOnly = useMemo(() => ({
		fetchNext: () => setCurrentPage(prev => prev + 1),
		fetchPrev: () => setCurrentPage(prev => prev - 1),
	}), [])

	return (
		<PokemonContextReadOnly.Provider value={{pokemonNames, currentPage}}>
			<PokemonContextWriteOnly.Provider value={pokemonContextWriteOnly}>
					{children}
			</PokemonContextWriteOnly.Provider>
		</PokemonContextReadOnly.Provider>
	)
}

export function PokemonListView() {
	const {pokemonNames, currentPage} = usePokemonContextReadOnly()

	return (
		<div>
			Page: {currentPage + 1}
			{pokemonNames.map(pokemonName => (
				<div key={pokemonName}>{pokemonName}</div>
			))}
		</div>
	)
}

export function PageControlButtons() {
	const {fetchPrev, fetchNext} = usePokemonContextWriteOnly()

	return (<>
			<button onClick={() => fetchPrev()}>prev</button>
			<button onClick={() => fetchNext()}>next</button>
	</>)
}