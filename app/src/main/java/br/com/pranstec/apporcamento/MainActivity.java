package br.com.pranstec.apporcamento;

import static java.lang.Double.parseDouble;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText edtNumeroOrcamento;
    EditText edtCliente;
    EditText edtEquip;
    EditText edtResponsavel;
    EditText edtServDesc;
    EditText edtQtdeServ;
    EditText edtValorUnitario;
    EditText edtDescMaterial;
    EditText edtQtdeMaterial;
    EditText edtValMaterial;
    EditText edtPrazaoMaterial;
    EditText edtDescritivo1;
    EditText edtDescritivo2;
    EditText edtDescritivo3;
    EditText edtDescritivo4;
    EditText edtPrazoEntServ;
    Button btnIncServ;
    Button btnLimpServ;
    Button btnIncMat;
    Button btnLimpMat;
    Button btnGerar;
    String[] servicos = new String[9];
    String[] qtdeItemServ = new String[9];
    String[] valorItemServ = new String[9];
    String[] materiais = new String[5];
    String[] qtdeItemMart = new String[5];
    String[] valorItemMart = new String[5];
    String[] prazoMaterial = new String[5];

    private static final int CREATEPDF = 1;

    int qtItem = 0;
    int qtMatrial = 0;

    Locale locate = new Locale("pt", "BR");
    Calendar calendar = Calendar.getInstance();
    int mes = calendar.get(Calendar.MONTH);
    int ano = calendar.get(Calendar.YEAR);
    int dia = calendar.get(Calendar.DAY_OF_MONTH);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniciarApp();

    }

    private void iniciarApp(){


        edtNumeroOrcamento = findViewById(R.id.edtNumeroOrcamento);
        edtCliente = findViewById(R.id.edtCliente);
        edtEquip = findViewById(R.id.edtEquip);
        edtResponsavel = findViewById(R.id.edtResponsavel);
        edtServDesc = findViewById(R.id.edtServDesc);
        edtQtdeServ = findViewById(R.id.edtQtdeServ);
        edtValorUnitario = findViewById(R.id.edtValorUnitario);
        edtDescMaterial = findViewById(R.id.edtDescMaterial);
        edtQtdeMaterial = findViewById(R.id.edtQtdeMaterial);
        edtValMaterial = findViewById(R.id.edtValMaterial);
        edtPrazaoMaterial = findViewById(R.id.edtPrazaoMaterial);
        edtDescritivo1 = findViewById(R.id.edtDescritivo1);
        edtDescritivo2 = findViewById(R.id.edtDescritivo2);
        edtDescritivo3 = findViewById(R.id.edtDescritivo3);
        edtDescritivo4 = findViewById(R.id.edtDescritivo4);
        edtPrazoEntServ = findViewById(R.id.edtPrazoEntServ);
        btnIncServ = findViewById(R.id.btnIncServ);
        btnLimpServ = findViewById(R.id.btnLimpServ);
        btnIncMat = findViewById(R.id.btnIncMat);
        btnLimpMat = findViewById(R.id.btnLimpMat);
        btnGerar = findViewById(R.id.btnGerar);
            }

    public void limparCampos() {

        edtNumeroOrcamento.setText("");
        edtCliente.setText("");
        edtEquip.setText("");
        edtResponsavel.setText("");
        edtDescritivo1.setText("");
        edtDescritivo2.setText("");
        edtDescritivo3.setText("");
        edtDescritivo4.setText("");
        edtPrazoEntServ.setText("");
        limparServicos();
        limparMateriais();
    }

    public String formatValor(String valor){
        valor = valor.replaceAll(",",".");
        return valor;
    }

    public void limparServicos(){
        edtServDesc.setText("");
        edtQtdeServ.setText("");
        edtValorUnitario.setText("");
    }

    public void limparMateriais(){
        edtDescMaterial.setText("");
        edtQtdeMaterial.setText("");
        edtValMaterial.setText("");
        edtPrazaoMaterial.setText("");
    }

    public void limparArrayServico(View view){
        limparServicos();
        for(int i = 0; i < servicos.length; i++){
            servicos[i] = "";
        }
        qtItem = 0;
    }

    public void limparArrayMateriais(View view){
        limparMateriais();
        for(int i = 0; i < materiais.length; i++){
            materiais[i] = "";
        }
        qtMatrial = 0;
    }

    public void incluirServico(View view) {

        if(qtItem < 7){
            servicos[qtItem] = edtServDesc.getText().toString();
            qtdeItemServ[qtItem] = formatValor(edtQtdeServ.getText().toString());
            valorItemServ[qtItem] = formatValor(edtValorUnitario.getText().toString());
            ++qtItem;
            String text = Integer.toString(qtItem);
            Toast.makeText(this, "Quantidade de Serviço inclusos "+text, Toast.LENGTH_SHORT).show();
            limparServicos();
        }else {
            Toast.makeText(this, "Só é permitido 7 serviços!", Toast.LENGTH_SHORT).show();
        }
    }

    public void incluirMaterias(View view) {

        if(qtMatrial < 5){
            materiais[qtMatrial] = edtDescMaterial.getText().toString();
            qtdeItemMart[qtMatrial] = formatValor(edtQtdeMaterial.getText().toString());
            valorItemMart[qtMatrial] = formatValor(edtValMaterial.getText().toString());
            prazoMaterial[qtMatrial] = edtPrazaoMaterial.getText().toString();
            ++qtMatrial;
            String text = Integer.toString(qtMatrial);
            Toast.makeText(this, "Quantidade de materiais inclusos "+text, Toast.LENGTH_SHORT).show();
            limparMateriais();
        }else {
            Toast.makeText(this, "Só é permitido 5 Materiais!", Toast.LENGTH_SHORT).show();
        }
    }

    public void gerarPDF(View view){
        if(verifica()) {
            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("application/pdf");
            intent.putExtra(Intent.EXTRA_TITLE, "Orçamento");
            startActivityForResult(intent, CREATEPDF);
        }
    }

    public Boolean verifica(){
        int error = 0;
        if(edtNumeroOrcamento.getText().toString().equals("")){
            error++;
        }
        if(edtCliente.getText().toString().equals("")){
            error++;
        }
        if(edtEquip.getText().toString().equals("")){
            error++;
        }
        if(edtResponsavel.getText().toString().equals("")){
            error++;
        }
        if(edtDescritivo1.getText().toString().equals("") && edtDescritivo2.getText().toString().equals("") &&
                edtDescritivo3.getText().toString().equals("") && edtDescritivo4.getText().toString().equals("") &&
                edtPrazoEntServ.getText().toString().equals("")){
            error++;
        }
        if(servicos.length == 0 ){
            error++;
        }
        if(materiais.length == 0 ){
            error++;
        }

        if(error > 0 ){
            Toast.makeText(this, "Todos os campos são obrigatórios", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CREATEPDF){
            if(data.getData() != null){
                String texto = edtCliente.getText().toString();
                Uri caminhoDoArquivo = data.getData();
                PdfDocument pdfDocument = new PdfDocument();
                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1240, 1754, 1).create();
                PdfDocument.Page page = pdfDocument.startPage(pageInfo);

                // Estilos
                Paint preto = new Paint();
                preto.setColor(Color.BLACK);
                Paint branco = new Paint();
                branco.setColor(Color.WHITE);
                int cinza = ContextCompat.getColor(this, R.color.cinza);
                int cinzacl = ContextCompat.getColor(this, R.color.cinzacl);
                Paint cinzaclaro = new Paint();
                cinzaclaro.setColor(cinza);
                Paint cinzamaisclaro = new Paint();
                cinzamaisclaro.setColor(cinzacl);

                Paint texto16 = new Paint();
                texto16.setTextSize(16f);

                Paint texto16Center = new Paint();
                texto16Center.setTextSize(16f);
                texto16Center.setTextAlign(Paint.Align.CENTER);

                Paint texto18= new Paint();
                texto18.setTextSize(18f);

                Paint texto18bold = new Paint();
                texto18bold.setTextSize(18f);
                texto18bold.setTypeface(Typeface.DEFAULT_BOLD);

                Paint texto20bold = new Paint();
                texto20bold.setTextSize(20f);
                texto20bold.setTypeface(Typeface.DEFAULT_BOLD);

                Paint texto18boldCenter = new Paint();
                texto18boldCenter.setTextSize(18f);
                texto18boldCenter.setTypeface(Typeface.DEFAULT_BOLD);
                texto18boldCenter.setTextAlign(Paint.Align.CENTER);

                Canvas canvas = page.getCanvas();

                //CONTEUDO DO pdf

                Bitmap logo = BitmapFactory.decodeResource(this.getResources(), R.drawable.logo);
                canvas.setDensity(40);
                canvas.drawBitmap(logo, 55,48, null);

                canvas.drawRect(880, 85,1150, 195, preto);
                canvas.drawRect(882, 87,1148, 193, branco);

                canvas.drawLine(50, 50, 1190, 50,preto);
                canvas.drawText("OCEÂNICA MOTORES",620,100,texto18boldCenter);
                canvas.drawText("Especializada em Motores e Equipamentos", 620, 120, texto16Center);
                canvas.drawText("End. RUA PARÁ 109 - CENTRO - São Sebastião SP", 620, 140, texto16Center);
                canvas.drawText("Contato: (12) 98274 0859", 620, 160, texto16Center);
                canvas.drawText("E-mail: oceanicamotores.oficina@gmail.com", 620, 180, texto16Center);

                canvas.drawText("Orçamento:", 918, 112, texto16);
                canvas.drawText(edtNumeroOrcamento.getText().toString()+"-"+(mes+1)+"-"+ano, 1025, 112, texto16);

                canvas.drawText("Data: " +dia+"/"+mes+"/"+ano, 918, 142, texto16);
                canvas.drawText("Aprovado:", 918, 172, texto16);

                canvas.drawLine(50, 210, 1190, 210, preto);
                canvas.drawLine(50, 215, 1190, 215, preto);

                canvas.drawText("CLIENTE: "+edtCliente.getText().toString(), 55, 235, texto18);
                canvas.drawText("EQUIPAMENTO/EMBARCAÇÃO: "+edtEquip.getText(), 55, 255, texto18);
                canvas.drawText("RESPONSÁVEL: "+edtResponsavel.getText(), 55, 275, texto18);

                canvas.drawLine(50, 290, 1190, 290, preto);
                canvas.drawLine(50, 291, 1190, 291, preto);

                canvas.drawRect(50, 300, 1190, 340, cinzaclaro);
                canvas.drawText("SERVIÇOS", 55, 325, texto18);

                canvas.drawLine(50, 350, 1190, 350, preto);
                canvas.drawText("Item",80, 367, texto16);
                canvas.drawText("Descrição",320, 367, texto16);
                canvas.drawText("QTD",650, 367, texto16);
                canvas.drawText("Valor",850, 367, texto16);
                canvas.drawText("Valor Total",1050, 367, texto16);
                canvas.drawLine(50, 375, 1190, 375, preto);

                int linesServices = 375;
                Double tGeralServ = 0.0;
                for(int i = 0; i < qtItem; i++) {
                    Double totalServ = parseDouble(qtdeItemServ[i]) * parseDouble(valorItemServ[i]);
                    tGeralServ= tGeralServ + totalServ;
                    String vlItemServ = NumberFormat.getCurrencyInstance().format(parseDouble(valorItemServ[i]));
                    String vlTotalCalc = NumberFormat.getCurrencyInstance().format(totalServ);

                    String item = Integer.toString(i + 1);
                    canvas.drawLine(50, linesServices, 1190, linesServices, cinzamaisclaro);
                    canvas.drawText(item, 95, linesServices + 20, texto16Center);
                    canvas.drawText(servicos[i], 355, linesServices + 20, texto16Center);
                    canvas.drawText(qtdeItemServ[i], 665, linesServices + 20, texto16Center);
                    canvas.drawText(vlItemServ, 867, linesServices + 20, texto16Center);
                    canvas.drawText(vlTotalCalc, 1090, linesServices + 20, texto16Center);

                    linesServices = linesServices + 40;
                }
                String valorFormatadoServ = NumberFormat.getCurrencyInstance().format(tGeralServ);
                canvas.drawText(valorFormatadoServ, 1045, 680, texto18bold);
                canvas.drawText("Total",950, 680, texto18bold);


                canvas.drawRect(50, 700, 1190, 740, cinzaclaro);
                canvas.drawText("MATERIAIS", 55, 725, texto18);

                canvas.drawLine(50, 750, 1190, 750, preto);
                canvas.drawText("Item",80, 767, texto16);
                canvas.drawText("Descrição",300, 767, texto16);
                canvas.drawText("QTD",580, 767, texto16);
                canvas.drawText("Valor Un",700, 767, texto16);
                canvas.drawText("Valor Total",850, 767, texto16);
                canvas.drawText("Prazo de Entrega",1050, 767, texto16);
                canvas.drawLine(50, 784, 1190, 784, preto);

                int linesMaterials = 784;
                Double tGeralMat = 0.0;
                for(int i = 0; i < qtMatrial; i++) {
                    Double totalMat = parseDouble(qtdeItemMart[i]) * parseDouble(valorItemMart[i]);
                    tGeralMat = tGeralMat + totalMat;
                    String vlItemMat = NumberFormat.getCurrencyInstance().format(parseDouble(valorItemMart[i]));
                    String vlTotalCalcMat = NumberFormat.getCurrencyInstance().format(totalMat);

                    String mat = Integer.toString(i + 1);
                    canvas.drawLine(50, linesMaterials, 1190, linesMaterials, cinzamaisclaro);
                    canvas.drawText(mat, 94, linesMaterials + 20, texto16Center);
                    canvas.drawText(materiais[i], 340, linesMaterials + 20, texto16Center);
                    canvas.drawText(qtdeItemMart[i], 597, linesMaterials + 20, texto16Center);
                    canvas.drawText(vlItemMat, 730, linesMaterials + 20, texto16Center);
                    canvas.drawText(vlTotalCalcMat, 894, linesMaterials + 20, texto16Center);
                    canvas.drawText(prazoMaterial[i], 1102, linesMaterials + 20, texto16Center);

                    linesMaterials = linesMaterials + 40;
                }


                String valorFormatadoMat = NumberFormat.getCurrencyInstance().format(tGeralMat);
                canvas.drawText(valorFormatadoMat, 1045, 1000, texto18bold);
                canvas.drawText("Total",950, 1000, texto18bold);
                canvas.drawRect(50, 1010, 1190, 1050, cinzaclaro);


                canvas.drawText("DESCRIÇÃO DOS SERVIÇOS QUE SERÃO EXECUTADOS:", 55, 1035, texto18);

                canvas.drawText(edtDescritivo1.getText().toString(),55, 1065, texto16);
                canvas.drawText(edtDescritivo2.getText().toString(),55, 1085, texto16);
                canvas.drawText(edtDescritivo3.getText().toString(),55, 1105, texto16);
                canvas.drawText(edtDescritivo4.getText().toString(),55, 1125, texto16);

                Double total = tGeralServ + tGeralMat;
                String valorFormatado = NumberFormat.getCurrencyInstance().format(total);
                canvas.drawRect(50, 1140, 1190, 1180, cinzaclaro);
                canvas.drawText("VALOR TOTAL DOS SERVIÇOS E MATERIAIS:", 55, 1165, texto20bold);
                canvas.drawText(valorFormatado, 1045, 1165, texto20bold);

                canvas.drawRect(50, 1190, 1190, 1230, cinzaclaro);
                canvas.drawText("FORMA DE PAGAMENTO VALOR DO SERVIÇO POR PIX OU DEPOSITO BANCARIO", 55, 1215, texto18);

                canvas.drawText("DADOS BANCÁRIOS: BANCO SANTANDER 033 | AG. 0103 | C.C 13003982-4", 55, 1250, texto16);
                canvas.drawText("CNPJ 41.060.490/0001-07 CHAVE PIX | OCEANICA MOTORES E EQUIPAMENTOS MARITIMOS", 55, 1270, texto16);

                canvas.drawRect(50, 1280, 1190, 1320, cinzaclaro);
                canvas.drawText("PRAZO DE ENTREGA", 55, 1308, texto18);

                canvas.drawText(edtPrazoEntServ.getText().toString()+" DIAS APÓS APROVAÇÃO", 620, 1340, texto16);

                canvas.drawRect(50, 1350, 1190, 1390, cinzaclaro);
                canvas.drawText("DISPOSIÇÕES FINAIS", 55, 1377, texto18);

                canvas.drawText("Validade desse orçamento: 15 dias a contar da data de emissão.", 55, 1410, texto16);
                canvas.drawText("As peças que não forem indicadas como IMEDIATO, considerar o prazo de entrega do fornecedor.", 55, 1430, texto16);
                canvas.drawText("Os serviços serão agendados após o pagamento da entrada e o recebimento de autorização do cliente.", 55, 1450, texto16);
                canvas.drawText("O prazo de entrega será de 07 DIAS APÓS A PROVAÇÃO DO ORÇAMENTO e poderá sofrer alteração, de acordo com a entrega de material pelo fornecedor.", 55, 1470, texto16);
                canvas.drawText("Quando houver necessidade técnica de inclusão de peças ou serviços, que não constem no orçamento inicial, o cliente será devidamente informado e deverá ", 55, 1490, texto16);
                canvas.drawText("autorizar as alterações especificadas.", 55, 1510, texto16);
                canvas.drawText("A GARANTIA dos serviços é de 90 dias após a entrega final.", 55, 1530, texto16);
                canvas.drawText("A GARANTIA dos materiais ou peças será conforme fabricante.", 55, 1550, texto16);


                canvas.drawText("APROVADO:          SIM( )               NÂO( )", 100, 1600, texto16);

                canvas.drawText("Data: ", 150, 1650, texto16);
                canvas.drawText("Assinatura cliente / Responsável: ", 450, 1650, texto16);

                pdfDocument.finishPage(page);
                gravarPDF(caminhoDoArquivo, pdfDocument);

            }
        }
    }

    private void gravarPDF(Uri caminhoDoArquivo, PdfDocument pdfDocument) {
        try {
            BufferedOutputStream stream = new BufferedOutputStream(getContentResolver().openOutputStream(caminhoDoArquivo));
            pdfDocument.writeTo(stream);
            pdfDocument.close();
            stream.flush();
            Toast.makeText(this, "PDF gerarado com sucesso!", Toast.LENGTH_SHORT).show();
        }catch (FileNotFoundException e) {
            Toast.makeText(this, "Erro 1"+e, Toast.LENGTH_SHORT).show();
        }catch (IOException e){
            Toast.makeText(this, "Erro 2"+e, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this, "Erro 3"+e, Toast.LENGTH_SHORT).show();
        }
        limparCampos();
        limparArrayServico(btnLimpServ);
        limparArrayMateriais(btnLimpMat);
    }


}