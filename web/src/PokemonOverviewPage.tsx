import PokemonRepository, { getPokemonNamesByOffset, NetworkPokemonRepository } from './PokemonRepository.ts'
import { useEffect, useState } from 'react'

const PokemonOverviewPage = ({pokemonRepository}: {pokemonRepository: PokemonRepository}) => {
	const [pokemonNames, setPokemonNames] = useState<string[]>([])

	useEffect(() => {
		pokemonRepository.getPokemonNamesByOffset(0)
			.then(pokemonNames => setPokemonNames(pokemonNames))
	}, [])

	return (
		<div>
			{pokemonNames.map(pokemonName => (
				<div key={pokemonName}>{pokemonName}</div>
			))}
		</div>
	)
}

export const PokemonOverviewPageWithoutDI = () => {
	const [pokemonNames, setPokemonNames] = useState<string[]>([])

	useEffect(() => {
		new NetworkPokemonRepository().getPokemonNamesByOffset(0)
			.then(pokemonNames => setPokemonNames(pokemonNames))
	}, [])

	return (
		<div>
			{pokemonNames.map(pokemonName => (
				<div key={pokemonName}>{pokemonName}</div>
			))}
		</div>
	)
}

export const PokemonOverviewPageWithoutClass = () => {
	const [pokemonNames, setPokemonNames] = useState<string[]>([])

	useEffect(() => {
		getPokemonNamesByOffset(0)
			.then(pokemonNames => setPokemonNames(pokemonNames))
	}, [])

	return (
		<div>
			{pokemonNames.map(pokemonName => (
				<div key={pokemonName}>{pokemonName}</div>
			))}
		</div>
	)
}

export default PokemonOverviewPage