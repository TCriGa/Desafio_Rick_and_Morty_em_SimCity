package br.com.zup.rickAndMortyEmSimCity.ui.characterfavorite.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import br.com.zup.rickAndMortyEmSimCity.BUNDLE_KEY
import br.com.zup.rickAndMortyEmSimCity.R
import br.com.zup.rickAndMortyEmSimCity.data.datasource.model.CharacterResult
import br.com.zup.rickAndMortyEmSimCity.databinding.FragmentCharacterFavoriteBinding
import br.com.zup.rickAndMortyEmSimCity.ui.characterfavorite.view.adapter.CharacterFavoriteAdapter
import br.com.zup.rickAndMortyEmSimCity.ui.characterfavorite.viewmodel.CharacterFavoriteViewModel
import br.com.zup.rickAndMortyEmSimCity.ui.viewstate.ViewState

class CharacterFavoriteFragment : Fragment() {
    private lateinit var binding: FragmentCharacterFavoriteBinding

    private val viewModel: CharacterFavoriteViewModel by lazy {
        ViewModelProvider(this)[CharacterFavoriteViewModel::class.java]
    }

    private val adapterList: CharacterFavoriteAdapter by lazy {
        CharacterFavoriteAdapter(arrayListOf(), this::goToCharacterDetail)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCharacterFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        exhibitRecycleView()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCharactersFavorite()
    }

    private fun initObserver() {
        viewModel.characterListFavoriteState.observe(this.viewLifecycleOwner) {
            when (it) {
                is ViewState.Success -> {
                    adapterList.updateCharacterList(it.data.toMutableList())
                    if (it.data.isEmpty()) {
                        Toast.makeText(context, getString(R.string.IsEmpty), Toast.LENGTH_LONG)
                            .show()
                    }
                }

                is ViewState.Error -> {
                    Toast.makeText(
                        context,
                        "${it.throwable.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {}
            }
        }
        viewModel.characterListDisfavorState.observe(this.viewLifecycleOwner) {
            when (it) {
                is ViewState.Success -> {
                    Toast.makeText(
                        context,
                        "O Personagem ${it.data.name} foi desfavoritado!",
                        Toast.LENGTH_SHORT
                    ).show()

                }
                is ViewState.Error -> {
                    Toast.makeText(
                        context,
                        "${it.throwable.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> {}
            }
        }
    }

    private fun exhibitRecycleView() {
        binding.rvFavoriteCharacter.adapter = adapterList
    }

    private fun goToCharacterDetail(characterResult: CharacterResult) {
        val bundle = bundleOf(BUNDLE_KEY to characterResult)
        NavHostFragment.findNavController(this)
            .navigate(R.id.action_characterFavoriteFragment_to_characterDetailFragment, bundle)
    }

}


