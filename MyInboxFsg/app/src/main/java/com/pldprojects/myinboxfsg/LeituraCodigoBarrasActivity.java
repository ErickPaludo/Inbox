package com.pldprojects.myinboxfsg;

import com.google.zxing.Result;
import com.smartdevicesdk.barcodescanner.zxing.ZXingScannerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class LeituraCodigoBarrasActivity extends Activity implements ZXingScannerView.ResultHandler
{
	private ZXingScannerView scannerView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		// Configurar o scanner view
		scannerView = new ZXingScannerView(this);
		setContentView(scannerView);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		// Iniciar a c�mera ao retomar a Activity
		scannerView.setResultHandler(this);
		scannerView.startCamera();
	}

	@Override
	public void onPause()
	{
		super.onPause();

		// Parar a c�mera ao pausar a Activity
		scannerView.stopCamera();
		scannerView = null;
	}

	@Override
	public void handleResult(Result result)
	{
		// // C�digo de barras lido com sucesso
		// Log.d("ZXing", "Resultado: " + result.getText());
		// Toast.makeText(this, "C�digo de Barras: " + result.getText(), Toast.LENGTH_SHORT).show();

		// Retomar a c�mera para continuar a leitura
		// scannerView.resumeCameraPreview(this);

		// Voc� pode enviar o resultado de volta para a Activity anterior usando Intent
		// Enviar o resultado de volta para a Activity anterior
		Intent intent = new Intent();
		intent.putExtra("SCAN_RESULT", result.getText());
		intent.putExtra("SCAN_RESULT_FORMAT", result.getBarcodeFormat().toString());
		setResult(RESULT_OK, intent);



		// Fechar a Activity ap�s a leitura bem-sucedida
		finish();
	}
}