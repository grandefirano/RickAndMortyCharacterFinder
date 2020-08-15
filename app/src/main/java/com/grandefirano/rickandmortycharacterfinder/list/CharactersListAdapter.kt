package com.grandefirano.rickandmortycharacterfinder.list

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.grandefirano.rickandmortycharacterfinder.data.Character
import com.grandefirano.rickandmortycharacterfinder.databinding.ListItemCharacterLayoutBinding



class CharactersListAdapter(private val clickListener:CharacterClickListener)
    : PagingDataAdapter<Character, CharactersListAdapter.ViewHolder>(CharacterDiffCallback()){

    companion object{
        const val TYPE_CHARACTER=1
        const val TYPE_FOOTER=2
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      val item=getItem(position)
        item?.let {
            holder.bind(it,clickListener)
        }
    }





    class ViewHolder private constructor(private val binding:ListItemCharacterLayoutBinding)
        :RecyclerView.ViewHolder(binding.root){

        fun bind(item:Character,clickListener: CharacterClickListener){
            Log.d("TAG", "bind: Binding")
            binding.character=item
            binding.clickListener=clickListener
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

class CharacterClickListener(val clickListener:(character:Character)->Unit){
    fun onClick(character:Character)=clickListener(character)
}