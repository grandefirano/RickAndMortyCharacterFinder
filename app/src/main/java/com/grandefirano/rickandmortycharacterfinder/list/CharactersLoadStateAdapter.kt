package com.grandefirano.rickandmortycharacterfinder.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.grandefirano.rickandmortycharacterfinder.R
import com.grandefirano.rickandmortycharacterfinder.databinding.ItemFooterCharactersLoadStateBinding
import retrofit2.HttpException
import java.net.UnknownHostException

class CharactersLoadStateAdapter(private val retry:()->Unit)
    : LoadStateAdapter<CharactersLoadStateAdapter.CharactersLoadStateViewHolder>() {

    override fun onBindViewHolder(holder: CharactersLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): CharactersLoadStateViewHolder {
        return CharactersLoadStateViewHolder.create(parent,retry)
    }

    class CharactersLoadStateViewHolder(
        private val binding:ItemFooterCharactersLoadStateBinding,
        retry:()->Unit
    ): RecyclerView.ViewHolder(binding.root) {

        init{
            binding.retryButton.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState:LoadState){
            if(loadState is LoadState.Error) {


                val exception=loadState.error.cause
                val message=if(exception is UnknownHostException){
                   "There is no connection with World Wide Web"
                }else{
                    "Error"
                }

                binding.errorMsg.text = message

            }
                binding.progressBar.isVisible = loadState is LoadState.Loading
                binding.retryButton.isVisible = loadState !is LoadState.Loading
                binding.errorMsg.isVisible = loadState !is LoadState.Loading

        }
        companion object {
            fun create(parent:ViewGroup,retry:()->Unit):CharactersLoadStateViewHolder{
                val view= LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_footer_characters_load_state,parent,false)
                val binding=ItemFooterCharactersLoadStateBinding.bind(view)
                return CharactersLoadStateViewHolder(binding,retry)
            }
        }

    }

}