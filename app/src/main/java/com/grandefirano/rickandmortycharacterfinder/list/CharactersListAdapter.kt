package com.grandefirano.rickandmortycharacterfinder.list

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.grandefirano.rickandmortycharacterfinder.data.Character
import com.grandefirano.rickandmortycharacterfinder.databinding.ListItemCharacterLayoutBinding

class CharactersListAdapter()
    :ListAdapter<Character,CharactersListAdapter.ViewHolder>(CharacterDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bind(getItem(position))
    }


    class ViewHolder private constructor(private val binding:ListItemCharacterLayoutBinding)
        :RecyclerView.ViewHolder(binding.root){

        fun bind(item:Character){
            Log.d("TAG", "bind: Binding")
            binding.character=item
            binding.executePendingBindings()

        }

        companion object{
            fun from(parent:ViewGroup):ViewHolder{
                val layoutInflater=LayoutInflater.from(parent.context)
                val binding=ListItemCharacterLayoutBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }

    }


}



class CharacterDiffCallback:DiffUtil.ItemCallback<Character>(){
    override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
       return oldItem.id==newItem.id
    }

    override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
        return oldItem==newItem
    }

}