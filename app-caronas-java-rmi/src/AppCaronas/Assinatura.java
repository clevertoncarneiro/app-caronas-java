package AppCaronas;

import java.security.*;

public class Assinatura
{
    // Gera e retorna chave publica e chave privada coerentes
    public KeyPair gerarChaves()
    {
        // SAeta o tipo de chave DSA
        KeyPairGenerator keyPairGen = null;
        try
        {
            keyPairGen = KeyPairGenerator.getInstance("DSA");
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        // Seta tamanho de chave utilizado e retorna chave
        if (keyPairGen != null)
        {
            keyPairGen.initialize(2048);

            return keyPairGen.generateKeyPair();
        }

        return null;
    }

    // Recebe string do arquivo com chave privada e retorna a assinadura
    public byte[] gerarAssinatura(byte[] arquivo, PrivateKey chavePrivada)
    {
        // Cria tipo de assinatura
        Signature sign = null;
        try
        {
            sign = Signature.getInstance("SHA256withDSA");
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        // Inicializa processo de assinatura
        try
        {
            sign.initSign(chavePrivada);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        // Adiciona a string a ser assinada
        try
        {
            sign.update(arquivo);
        }
        catch (SignatureException e)
        {
            e.printStackTrace();
        }

        // Assina a string
        byte[] assinatura = new byte[0];
        try
        {
            assinatura = sign.sign();
        }
        catch (SignatureException e)
        {
            e.printStackTrace();
        }

        // Retorna assinatura correspondente
        return assinatura;
    }

    // Com a assinatura, arquivo original e a chave publica, retorna se e valido.
    public boolean verificarAssinatura(byte[] arquivo, byte[] assinatura, PublicKey chavePublica)
    {
        Signature sign;
        boolean FlagEhValido = false;

        try
        {
            // Instancia a assinatura
            sign = Signature.getInstance("SHA256withDSA");
            // Inicializa estrutura
            sign.initVerify(chavePublica);
            // Atualiza com o arquivo original
            sign.update(arquivo);
            // Verifica com a assinatura
            FlagEhValido = sign.verify(assinatura);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return FlagEhValido;
    }
}
