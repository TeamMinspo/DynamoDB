import { afterEach, describe, expect, test, vi } from 'vitest'
import { act, render, screen } from '@testing-library/react'
import { PokemonOverviewPageWithoutClass } from './PokemonOverviewPage.tsx'
import { getPokemonNamesByOffset } from './PokemonRepository.ts'
import * as PokemonRepository from './PokemonRepository.ts'


vi.mock('./PokemonRepository', () => {
	return {
		getPokemonNamesByOffset: vi.fn().mockResolvedValue([])
	}
})

describe('pokemon', () => {
	afterEach(() => {
		vi.resetAllMocks()
	})

	test('show pokemon', async () => {
		vi.mocked(getPokemonNamesByOffset).mockResolvedValue([
			'不思議だね',
			'みずがめ',
		])
		await act(async () => {
			render(<PokemonOverviewPageWithoutClass/>)
		})


		expect(screen.getByText('不思議だね')).toBeInTheDocument()
		expect(screen.getByText('みずがめ')).toBeInTheDocument()
		expect(getPokemonNamesByOffset).toHaveBeenCalledTimes(1)
	})

	test('show pokemon2', async () => {
		vi.mocked(getPokemonNamesByOffset).mockResolvedValue([
			'不思議だね',
			'みずがめ',
		])
		await act(async () => {
			render(<PokemonOverviewPageWithoutClass/>)
		})


		expect(screen.getByText('不思議だね')).toBeInTheDocument()
		expect(screen.getByText('みずがめ')).toBeInTheDocument()
		expect(getPokemonNamesByOffset).toHaveBeenCalledTimes(1)
	})

	test('spyOn', async () => {
		vi.spyOn(PokemonRepository, 'getPokemonNamesByOffset').mockResolvedValue([
			'不思議だね',
			'みずがめ',
		])
		await act(async () => {
			render(<PokemonOverviewPageWithoutClass/>)
		})


		expect(screen.getByText('不思議だね')).toBeInTheDocument()
		expect(screen.getByText('みずがめ')).toBeInTheDocument()
		expect(getPokemonNamesByOffset).toHaveBeenCalledTimes(1)
	})

	test('spyOn2', async () => {
		vi.spyOn(PokemonRepository, 'getPokemonNamesByOffset').mockResolvedValue([
			'不思議だね',
			'みずがめ',
		])
		await act(async () => {
			render(<PokemonOverviewPageWithoutClass/>)
		})


		expect(screen.getByText('不思議だね')).toBeInTheDocument()
		expect(screen.getByText('みずがめ')).toBeInTheDocument()
		expect(getPokemonNamesByOffset).toHaveBeenCalledTimes(1)
	})
})
