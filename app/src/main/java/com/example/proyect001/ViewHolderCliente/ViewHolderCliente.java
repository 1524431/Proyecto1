package com.example.proyect001.ViewHolderCliente;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyect001.R;

public class ViewHolderCliente extends RecyclerView.ViewHolder {
    View mView;
    private ClickListener mClickListener;

    // Interface para manejar los clics en el item
    public interface ClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public ViewHolderCliente(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        // Evento de clic corto
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mClickListener != null) {
                    mClickListener.onItemClick(view, getAdapterPosition());
                }
            }
        });

        // Evento de clic largo
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mClickListener != null) {
                    mClickListener.onItemLongClick(view, getAdapterPosition());
                }
                return true; // Cambiado a true para indicar que el evento se ha manejado
            }
        });
    }

    // Método para establecer el listener desde fuera de la clase
    public void setClickListener(ClickListener clickListener) {
        this.mClickListener = clickListener;
    }
    public void setearDatosCliente(Context context, String idCliente, String uidCliente,
                                   String nombres, String apellidos, String correo,
                                   String dni, String telefono, String direccion) {
        ImageView ivClienteFoto;
        TextView tvidclientel, tvuidclientel, tvnombrel, tvApellido, tvcorreoclientel, tvdniclientel, tvtelefonoclientel, tvdireccionclientel;
        ivClienteFoto = mView.findViewById(R.id.ivclientefotol);
        tvidclientel = mView.findViewById(R.id.tvidclientel);
        tvuidclientel = mView.findViewById(R.id.tvuidclientel);
        tvnombrel = mView.findViewById(R.id.tvnombrel);
        tvApellido = mView.findViewById(R.id.tvapellidol);
        tvcorreoclientel = mView.findViewById(R.id.tvcorreoclientel);
        tvdniclientel = mView.findViewById(R.id.tvdniclientel);
        tvtelefonoclientel = mView.findViewById(R.id.tvtelefonoclientel);
        tvdireccionclientel = mView.findViewById(R.id.tvdireccionclientel);

        tvidclientel.setText(idCliente);
        tvuidclientel.setText(uidCliente);
        tvnombrel.setText(nombres);
        tvApellido.setText(apellidos);
        tvcorreoclientel.setText(correo);
        tvdniclientel.setText(dni);
        tvtelefonoclientel.setText(telefono);
        tvdireccionclientel.setText(direccion);
    }

}

