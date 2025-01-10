import { describe, expect, test, vi } from 'vitest'
import { act, render } from '@testing-library/react'
import {screen} from '@testing-library/react'
import PokemonOverviewPage, { PokemonOverviewPageWithoutDI } from './PokemonOverviewPage.tsx'
import PokemonRepository, { NetworkPokemonRepository } from './PokemonRepository.ts'

class StubPokemonRepository implements PokemonRepository {
		getPokemonNamesByOffset_returnValue: Promise<string[]> = Promise.resolve([])
    async getPokemonNamesByOffset(offset: number): Promise<string[]> {
        return this.getPokemonNamesByOffset_returnValue
    }
}

const mockGetPokemonNamesByOffset = vi.fn()

vi.mock('./PokemonRepository', async (importOriginal) => {
	const original = await importOriginal<typeof import('./PokemonRepository.ts')>()

	class MockPokemonRepository extends original.NetworkPokemonRepository {
		getPokemonNamesByOffset = mockGetPokemonNamesByOffset
	}

	return {
		NetworkPokemonRepository: MockPokemonRepository
	}
})

describe.skip('PokemonOverviewPage', () => {
	test('自作テストダブルDI', async () => {
		const stubPokemonRepository = new StubPokemonRepository()
		stubPokemonRepository.getPokemonNamesByOffset_returnValue = Promise.resolve([
			'不思議だね',
			'みずがめ',
		])
		await act(async () => {
			render(<PokemonOverviewPage pokemonRepository={stubPokemonRepository}/>)
		})

		expect(screen.getByText('不思議だね')).toBeInTheDocument()
		expect(screen.getByText('みずがめ')).toBeInTheDocument()
	})

	test('クラスのメソッドをモック化', async () => {
		vi.spyOn(NetworkPokemonRepository.prototype, 'getPokemonNamesByOffset').mockResolvedValue([
			'不思議だね',
			'みずがめ',
		])
		await act(async () => {
			render(<PokemonOverviewPageWithoutDI/>)
		})

		expect(screen.getByText('不思議だね')).toBeInTheDocument()
		expect(screen.getByText('みずがめ')).toBeInTheDocument()
	})

	test('クラスのメソッドをモック化(vi.mock)', async () => {
		mockGetPokemonNamesByOffset.mockResolvedValue([
			'不思議だね',
			'みずがめ',
		])
		await act(async () => {
			render(<PokemonOverviewPageWithoutDI/>)
		})

		expect(screen.getByText('不思議だね')).toBeInTheDocument()
		expect(screen.getByText('みずがめ')).toBeInTheDocument()
	})
})