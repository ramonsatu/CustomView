# CustomView
Use este projeto como desejar. Melhorias são bem vindas.

# Column Chart View

As "Custom Views" são desafiadoras e para estar preparado para encará-las , deve-se ter em mente o ciclo de vida da View e OOP.

# Ciclo de Vida da View e OOP

Uma visualização que foi renderizada na tela deve passar por esses métodos de ciclo de vida para ser desenhada na tela.

![image](https://github.com/ramonsatu/CustomView/assets/117767174/6838ec22-f185-433c-9462-83c1bb399600)



As funções que nós vamos sobrescrever serão :

![image](https://github.com/ramonsatu/CustomView/assets/117767174/f33545f5-4b3e-425d-a791-dee4f54cf6b5)


Antes de falar sobre cada uma delas, devemos lembrar de um dos piláres da Programação Orientada a Objetos, a Herança.

O que a Herança tem a ver com isso?

Para termos acesso aos métodos devemos ter a View como pai da nossa classe. 

Ao fazermos isso devemos escolher qual dos construtores vamos utilizar:

* 1- View(Context context) 
* 2- View(Context context, @Nullable AttributeSet attrs) 
* 3- View(Context context, @Nullable AttributeSet attrs, int defStyleAttr) 
* 4- View(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes)

No nosso caso, vamos utilizar o segundo construtor que é chamado ao inflarmos a view do XML. Ou seja, quando uma view estar sendo  construida apartir de um arquivo XML nos precisamos fornercer os atributos especificados.

Se agora você estar se perguntando: como vou ter acesso a esses atributos dentro do editor de layout xml?

Você precisa de um "declare-styleable". Pode criá-lo dentro do diretório res/values, dentro dele é onde estará o acesso aos atributos:

![image](https://github.com/ramonsatu/CustomView/assets/117767174/5536c13f-ffba-42ed-afcf-37dfe5e21a9a)

O nome do styeable deve ser o mesmo que  foi dado a class da vizualiação customizada.

Criado o styeable , agora, você precisa de um typedArray para armazenar os atributos.

![image](https://github.com/ramonsatu/CustomView/assets/117767174/9e2eb0fa-d1af-447d-8fb8-7aecd1292303)

No bloco init é onde você deve passar os valores dos atributos que virão do typedArray para os atributos da sua CustomView.

    init  {
       try {
            xAxisColor = typedArray.getColor(R.styleable.ColumnChartView_xAxis_Color, Color.BLACK)
            yAxisColor = typedArray.getColor(R.styleable.ColumnChartView_yAxis_Color, Color.BLACK)
            expandYAxisBase = typedArray.getDimension(R.styleable.ColumnChartView_expandYAxisBase, 0f)
            expandYAxisTop = typedArray.getDimension(R.styleable.ColumnChartView_expandYAxisTop, 0f)
            ...
        }  finally {
                typedArray.recycle()
        }
      }

Envolva as suas atribuições em um bloco try{}catch{}, dentro do catch{} declare "typedArray.recycle()".

Agora que sabemos o porquê da herança e como ter acesso aos atributos que são modificados dentro do editor de layout. Estamos prontos para entender o que faremos dentro das funções.

# Fun onAttachedToWindow()

Aqui é onde nós podemos alocar recursos e configurar ouvintes, já que a view esta anexada a  janela.

     override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        cPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        cPaint?.style = Paint.Style.FILL
        cPaint!!.strokeWidth = strokeWidth
        sscale = (height - (spaceBetweenLinesXAxis * (numberOfItemsYAxis - 1))) / -100f
    }

# Fun onDetachedFromWindow()

Neste ponto, a view não estar mais anexada a janela. É neste lugar onde você precisa limpar os recursos que estão alocados e parar de fazer qualquer tipo de trabalho agendado.

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        cPaint = null
    }

# Fun onMeasure()

Essa função e chamada para sabermos o tamanho da view. Você deve tratar questões  como: a sua view vai mudar de tamanho quando mudar a orientação da tela ou qual o valor minino ela terá quando estiver defino warp_content para width e height. No final, vai depender do que você deseja fazer.

      override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        when (layoutParams.width) {
            ViewGroup.LayoutParams.WRAP_CONTENT -> {
                val r = resources
                val value = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 252f, r.displayMetrics
                )
                viewWidth = value.toInt()
            }

            ViewGroup.LayoutParams.MATCH_PARENT -> {
                viewWidth = min(
                    MeasureSpec.getSize(widthMeasureSpec),
                    MeasureSpec.getSize(heightMeasureSpec)
                )

            }

            else -> {
                viewWidth = widthMeasureSpec
            }
        }

        when (layoutParams.height) {
            ViewGroup.LayoutParams.WRAP_CONTENT -> {
                val r = resources
                val value = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 212f, r.displayMetrics
                )
                viewHeight = value.toInt()
            }

            ViewGroup.LayoutParams.MATCH_PARENT -> {
                viewHeight = min(
                    MeasureSpec.getSize(widthMeasureSpec),
                    MeasureSpec.getSize(heightMeasureSpec)
                )
            }

            else -> {
                viewHeight = heightMeasureSpec
            }
        }
        setMeasuredDimension(viewWidth, viewHeight)
    }
É importante deixar claro que esta função não retona valor. Logo, você deve chamar "setMeasureDimension" e passar os valores da largura e altura da view.

# Fun onDraw()

Na chamada anterior (onMeasure()) nos calculamos os tamanhos e(ou) posições para que possamos  desenhar dentro desses limites. No onDraw o objeto Canvas possui uma lista de comandos que podemos usar para desenhar na tela. Recomenda-se não alocar objetos em onDraw(), visto que ele é chamado várias vezes.

    override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)

    //Desenha rentangulos ou quadrados na tela
    canvas.drawRect(left, top, right, bottom, paint)

    //Desenha textos
    canvas.drawText(text, x, y, paint)

    }

Nós estruturamos logicamente uma comibinação desses dois métodos para criar o gráfico de coluna a seguir:

![image](https://github.com/ramonsatu/CustomView/assets/117767174/c95323ca-4c5a-4414-bb5a-3d79f7bde98e)

Vale lembrar que temos um plano cartesiano como tela em branco. É importante que tenha em mente os conceitos de pares ordenados, produto cartesiano, funções.

# invalidate()

O invalidate() precisa ser chamando quando houver uma mudança que afeta a aparência da view. Ex: Alterar os valores padrões das colunas. Esse método força o redesenho de uma determinada view para mostrar as alterações que foram recebidas.

# requestLayout()

Este método é chamando para lidar com mudanças que afetam a largura e altura da view. O requestLayout() é o sinal para o sistema de que ele precisa recalcular os limites da view.

# Não esquecer!
Por fim , não esqueça, que você deve salvar o estado da view quando necessário.